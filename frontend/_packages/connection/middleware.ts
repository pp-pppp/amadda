import { UserAccessResponse, UserJwtResponse } from 'amadda-global-types';
import axios from 'axios';
import type { AxiosResponse } from 'axios';
import { NextRequest, NextResponse } from 'next/server';
import { REDIS } from '../../_packages/connection';

const targetPath = [
  '/',
  '/main',
  '/api/auth/signin',
  '/api/auth/signout',
  '/api/auth/init',
];

const middleware = async (req: NextRequest) => {
  const { pathname } = req.nextUrl; //포트 이후의 api 요청입니다.

  //정적 자원 요청은 통과시킵니다.
  if (pathname.startsWith('/_next/static/')) return NextResponse.next();

  //targetPath와 일치하는 pathname요청이 들어온 경우 통과시킵니다.
  if (targetPath.includes(pathname)) return NextResponse.next();

  if (!req.cookies.has('Auth'))
    return NextResponse.rewrite('/api/auth/signout'); //만약 쿠키의 액세스 토큰이 아예 없다면 로그아웃시킵니다.

  const AT = req.cookies.get('Auth')?.value || ''; //쿠키의 access token입니다.

  const preflight: AxiosResponse<UserAccessResponse> = await axios
    .get(`${process.env.SPRING_ROOT as string}/user/access`, {
      headers: { Cookie: `Auth=${AT}` }, //프리플라이트 요청을 보냅니다. 본 요청을 보내기 전에 헤더만 떼어 미리 보내고, 토큰이 어떤 상태인지 백엔드에 확인받습니다.
      withCredentials: true,
    })
    .then(res => res)
    .catch(err => err.status);

  if (preflight.data.isBroken || !preflight)
    //토큰이 만료된 것이 아니라 손상되었다면 로그아웃시킵니다.
    return NextResponse.rewrite('/api/auth/signout');
  if (preflight.data.isExpired) {
    //토큰이 만료된 것이라면 리프레시 토큰을 확인할 수 있는 key가 제공됩니다.
    const RT =
      (await REDIS.getRefreshToken(preflight.data.refreshAccessKey)) || ''; //리프레시 토큰은 레디스에 있습니다. 레디스에서 key를 통해 토큰을 꺼냅니다.

    const re_preflight: AxiosResponse<UserJwtResponse> = await axios
      .get(`${process.env.SPRING_ROOT as string}/user/refresh`, {
        headers: { Auth: RT }, //Auth 헤더에 RT를 넣어서 백엔드에 확인받습니다.
      })
      .then(res => res)
      .catch(err => err);
    if (re_preflight.status > 300)
      //400번대 이상의 응답이 온다면 리프레시 토큰에 문제가 있는 것이고,
      //300번대 응답이 온다면 문제가 있는 상황이므로 로그아웃시킵니다
      return NextResponse.rewrite('/api/auth/signout');
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
      //다른 모든 경우의 경우 로그아웃시킵니다.
      return NextResponse.rewrite('/api/auth/signout');
    }
  }
  //만약 처음에 받았던 액세스 토큰에 문제가 없었다면 요청을 그대로 통과시킵니다.
  const response = NextResponse.next();
  return response;
};
export default middleware;
