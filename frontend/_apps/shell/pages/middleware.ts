import { REDIS } from '@/connection/connection';
import axios, { Axios, AxiosResponse } from 'axios';
import { NextRequest, NextResponse } from 'next/server';

export const tokenValidation = async (req: NextRequest) => {
  const { pathname } = req.nextUrl;
  const excludePath = ['/api/auth/signin', '/api/auth/signout'];
  if (excludePath.includes(pathname)) return NextResponse.next();
  // 현재 경로가 제외 목록에 있으면 그대로 요청을 진행합니다
  if (req.cookies.has('at') === false)
    return NextResponse.rewrite(
      new URL(
        'http://localhost:3000/api/auth/signout',
        process.env.ROOT as string
      )
    ); //만약 쿠키의 액세스 토큰이 없다면 로그아웃시킵니다.

  const AT = req.cookies.get('at')?.value || ''; //쿠키의 access token입니다.
  req.cookies.delete('at'); //쿠키의 access token을 삭제합니다.

  const preflight: AxiosResponse<UserAccessResponse> = await axios
    .get(`${process.env.SPRING_ROOT as string}/user/access`, {
      headers: { Auth: AT }, //프리플라이트 요청을 보냅니다. 본 요청을 보내기 전에 헤더만 떼어 미리 보내고, 토큰이 어떤 상태인지 백엔드에 확인받습니다.
    })
    .then(res => res)
    .catch(err => err.status);

  if (preflight.data.isBroken || !preflight)
    //토큰이 만료된 것이 아니라 손상되었다면 로그아웃시킵니다.
    return NextResponse.rewrite(
      new URL(
        'http://localhost:3000/api/auth/signout',
        process.env.ROOT as string
      )
    );
  if (preflight.data.isExpired) {
    //토큰이 만료된 것이라면 리프레시 토큰을 확인할 수 있는 key가 제공됩니다.
    const RT =
      (await REDIS.getRefreshToken(preflight.data.refreshAccessKey)) || ''; //리프레시 토큰은 레디스에 있습니다. 레디스에서 key를 통해 토큰을 꺼냅니다.

    const re_preflight: AxiosResponse<UserJwtResponse> = await axios
      .get(`${process.env.SPRING_ROOT as string}/user/refresh`, {
        headers: { Auth: RT }, //Auth 헤더에 RT를 넣어서 백엔드에 확인받습니다.
      })
      .then(res => {
        if (res.status === 401) throw new Error('401');
        //만약 상태코드가 401이라면 리프레시 토큰이 만료되었거나 손상된 것입니다.
        else return res;
      })
      .catch(err => err);
    if (re_preflight.status < 300) {
      //그렇지 않다면 (truthy한 값이 왔다면) 토큰 두 개와, 리프레시 토큰을 저장할 key가 함께 제공됩니다.
      REDIS.setRefreshToken(
        re_preflight.data.refreshAccessKey,
        re_preflight.data.refreshToken
      ); //리프레시 토큰을 레디스에 저장하고,
      const new_AT = re_preflight.data.accessToken; //새로운 액세스 토큰을 넣어서
      const response = NextResponse.next();
      response.headers.set('Auth', new_AT);
      return response; //원래 요청을 재시도합니다.
    } else {
      //리프레시 토큰이 만료되었거나 손상되었다면 로그아웃시킵니다.
      return NextResponse.rewrite(
        new URL('http://localhost:3000/api/auth/signout')
      ); //logout API
    }
  }
  console.log('통과');
  //만약 처음에 받았던 액세스 토큰에 문제가 없었다면
  const response = NextResponse.next();
  response.headers.set('Auth', AT);
  return response;
  //헤더만 바꿔 요청을 그대로 통과시킵니다.
};

export const injectCookie = async (res: NextResponse) => {
  const AT = res.headers.get('Auth');
  if (AT) {
    res.cookies.set('at', AT, {
      httpOnly: true,
      sameSite: 'strict',
    });
    res.headers.delete('Auth');
  }
  return res;
};

interface UserAccessResponse {
  isExpired: boolean;
  isBroken: boolean;
  refreshAccessKey: string;
}

interface UserJwtResponse {
  accessToken: string;
  refreshToken: string;
  refreshAccessKey: string;
}
