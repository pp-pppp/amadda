import { gateway, middlewareConfig } from 'connection';
import { NextRequest } from 'next/server';

export const config = middlewareConfig;
export function middleware(req: NextRequest) {
  return gateway(req);
}
