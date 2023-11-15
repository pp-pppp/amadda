import { gateway } from 'connection';
import { NextRequest } from 'next/server';

export default function middleware(req: NextRequest) {
  return gateway(req);
}
