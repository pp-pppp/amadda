import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';
import { withAuth, http } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    try {
      const { code, message, data } = await http.get<string>(`${process.env.SPRING_API_ROOT}/schedule/server-time`);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'schedule/api/schedule/server-time');
