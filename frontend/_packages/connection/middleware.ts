import { NextRequest, NextResponse } from 'next/server';

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
    return NextResponse.redirect(`${process.env.NEXT_PUBLIC_SHELL}/signout`); //만약 쿠키의 액세스 토큰이 없다면 로그아웃시킵니다.

  return NextResponse.next();
};
export default gateway;
