import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, AlarmReadResponse } from '@amadda/global-types';
import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { alarm_seq } = req.query;
  if (req.method === 'POST') {
    try {
      const { code, message, data } = await https.get<AlarmReadResponse>(`${process.env.SPRING_API_ROOT}/alarm/${alarm_seq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'notice/api/alarm/[alarm_seq]');
