import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from '@amadda/fetch';
import type { ApiResponse } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //유저 로그아웃 용 카카오id 받기
    try {
      const { code, message, data } = await https.get<string>(`${process.env.SPRING_API_ROOT}/user/logout`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(handler, 'shell/api/logout');
