import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';
import type { ApiResponse } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //유저 로그아웃 용 카카오id 받기
    try {
      const { status, message, data } = await https.get<ApiResponse<string>>(`${process.env.SPRING_API_ROOT}/user/logout`, token);
      return res.status(status).json(data);
    } catch (err) {
      console.log(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(handler, 'shell/api/logout');
