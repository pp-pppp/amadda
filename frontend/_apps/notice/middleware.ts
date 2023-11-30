import { gateway } from 'connection';
import { NextRequest } from 'next/server';

export const config = {
  matcher: ['/((?!api/auth|api/user/login|api/user/signup|api/user/access|api/user/refresh|api/user/check|_next/static|_next/image|favicon.ico|image).*)'],
};

export function middleware(req: NextRequest) {
  return gateway(req);
}
