import { UserAccessResponse, UserJwtResponse } from '@amadda/global-types';
import type { NextApiRequest, NextApiResponse } from 'next';
import * as Sentry from '@sentry/nextjs';
import { kv } from './kv';
import { https } from './https';

type API_HANDLER = (request: NextApiRequest, response: NextApiResponse) => Promise<NextApiResponse | void>;

/**
 * 클라이언트 사이드의 통신 규격은 쿠키, 서버 사이드의 통신 규격은 Bearer Auth입니다.
 * 따라서 next.js에서는 클라이언트 요청을 spring으로 보낼 때 쿠키를 header로 변환하고,
 * 응답을 받아 쿠키를 지속적으로 갱신해줘야 합니다.
 */
export function withAuth(fn: API_HANDLER): API_HANDLER {
  return async function (req: NextApiRequest, res: NextApiResponse) {
    let ACCESS_TOKEN = req.cookies.Auth || '';
    if (!req.cookies.Auth) {
      return res.redirect(307, `${process.env.NEXT_PUBLIC_SHELL}`);
    }
    try {
      ACCESS_TOKEN = await VERIFIED_TOKEN(ACCESS_TOKEN);
      req.headers.authorization = `Bearer ${ACCESS_TOKEN}`;
      await fn(req, res);
      return res.setHeader('Set-Cookie', `Auth=${ACCESS_TOKEN}; Max-Age=900; HttpOnly; SameSite=Lax;`);
    } catch (err) {
      Sentry.captureException(err);
      res.setHeader('Set-Cookie', `Auth=; Max-Age=0;`);
      return res.redirect(307, `${process.env.NEXT_PUBLIC_SHELL}`);
    }
  };
}

async function VERIFY(type: 'access' | 'refresh', token: string): Promise<any> {
  const BEARER_TOKEN = `Bearer ${token}`;
  const { code, message, data } = await https.get(`${process.env.SPRING_API_ROOT as string}/user/${type}`, BEARER_TOKEN);
  return data;
}

async function VERIFIED_TOKEN(accessToken: string): Promise<string> {
  const inspect_AT: UserAccessResponse = await VERIFY('access', accessToken);
  if (inspect_AT.isBroken || !inspect_AT) throw new Error('Broken Token');
  if (inspect_AT.isExpired) {
    const RT = (await kv.getToken(inspect_AT.refreshAccessKey)) || '';
    const inspect_RT: UserJwtResponse = await VERIFY('refresh', RT);
    kv.setToken(inspect_RT.refreshAccessKey, inspect_RT.refreshToken);
    return inspect_RT.accessToken;
  }
  return accessToken;
}
