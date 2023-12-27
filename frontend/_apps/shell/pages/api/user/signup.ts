import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, UserInitRequest, UserJwtResponse } from 'amadda-global-types';
import cookie from 'cookie';
import { kv, http } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //첫 로그인시 사용자 회원가입
    try {
      const { code, message, data } = await http.post<UserInitRequest, UserJwtResponse>(`${process.env.SPRING_API_ROOT}/user/signup`, req.body);

      const COOKIE = cookie.serialize('Auth', data.accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV !== 'development',
        path: '/',
        sameSite: 'lax',
        maxAge: 60 * 15,
      });

      res.setHeader('Set-Cookie', COOKIE);
      await kv.setToken(data.refreshAccessKey, data.refreshToken);

      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(handler, 'shell/api/signup');
