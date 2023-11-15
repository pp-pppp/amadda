import gateway from 'connection/middleware';
import { NextRequest } from 'next/server';

export default function middleware(req: NextRequest) {
  return gateway(req);
}
