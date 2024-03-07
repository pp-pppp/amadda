import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, http } from '@amadda/fetch';
import type { ApiResponse } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //현재 서버 시간 반환
    try {
      const { code, message, data } = await http.get<string>(`${process.env.SPRING_API_ROOT}/schedule/server-time`);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), '');
