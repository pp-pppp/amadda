import { gateway } from 'connection';
import { NextRequest } from 'next/server';

export const config = {
  matcher: ['/((?!api/auth|api/user|_next/static|_next/image|favicon.ico).*)'],
};

export function middleware(req: NextRequest) {
  return gateway(req);
}
