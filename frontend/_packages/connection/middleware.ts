import type { UserAccessResponse, UserJwtResponse } from 'amadda-global-types';
import { NextRequest, NextResponse } from 'next/server';
import { KV } from '../../_packages/connection';

export const middlewareConfig = {
  matcher: [
    '/((?!api/auth|api/user|_next/static|_next/image|favicon.ico|image).*)',
  ],
};

export const gateway = async (req: NextRequest) => {
  const { pathname } = req.nextUrl;
  if (pathname === '/') return NextResponse.next();
  if (pathname === '/signup') return NextResponse.next();

  if (!req.cookies.has('Auth'))
    return NextResponse.rewrite(
      `${process.env.NEXT_PUBLIC_SHELL}/api/auth/signout`
    ); //만약 쿠키의 액세스 토큰이 없다면 로그아웃시킵니다.

  const AT = req.cookies.get('Auth')?.value || ''; //쿠키의 access token입니다.

  const preflight: UserAccessResponse = await fetch(
    `${process.env.SPRING_API_ROOT as string}/user/access`,
    {
      headers: { Cookie: `Auth=${AT}` }, //프리플라이트 요청을 보냅니다. 본 요청을 보내기 전에 헤더만 떼어 미리 보내고, 토큰이 어떤 상태인지 백엔드에 확인받습니다.
    }
  )
    .then(res => res.json())
    .catch(err => err.status);

  if ((await preflight.isBroken) || !preflight)
    //토큰이 만료된 것이 아니라 손상되었다면 로그아웃시킵니다.
    return NextResponse.rewrite(
      `${process.env.NEXT_PUBLIC_SHELL}/api/auth/signout`
    );
  if (preflight.isExpired) {
    //토큰이 만료된 것이라면 리프레시 토큰을 확인할 수 있는 key가 제공됩니다.
    try {
      const RT = (await KV.getRefreshToken(preflight.refreshAccessKey)) || ''; //리프레시 토큰은 레디스에 있습니다. 레디스에서 key를 통해 토큰을 꺼냅니다.

      const re_preflight: UserJwtResponse = await fetch(
        `${process.env.SPRING_ROOT as string}/user/refresh`,
        {
          headers: { Cookie: `Auth=${RT}` }, //Auth 헤더에 RT를 넣어서 백엔드에 확인받습니다.
        }
      ).then(res => res.json());
      //프리플라이트 응답이 잘 왔다면

      KV.setRefreshToken(
        re_preflight.refreshAccessKey,
        re_preflight.refreshToken
      ); //리프레시 토큰을 레디스에 저장하고,

      const new_AT = re_preflight.accessToken; //새로운 액세스 토큰을 넣어서
      const response = NextResponse.next();
      response.cookies.set('Auth', new_AT, {
        httpOnly: true,
        sameSite: 'lax',
        maxAge: 60 * 15,
        secure: process.env.NODE_ENV !== 'development',
        path: '/',
      });

      return response; //원래 요청을 재시도합니다.
    } catch (err) {
      //400번대 이상의 응답이 온다면 리프레시 토큰에 문제가 있는 것이고,
      //300번대 응답이 온다면 문제가 있는 상황이므로 로그아웃시킵니다
      return NextResponse.rewrite(
        `${process.env.NEXT_PUBLIC_SHELL}/api/auth/signout`
      );
    }
  }
  //만약 처음에 받았던 액세스 토큰에 문제가 없었다면 요청을 그대로 통과시킵니다.
  const response = NextResponse.next();
  return response;
};
export default gateway;
