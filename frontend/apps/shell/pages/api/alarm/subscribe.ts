import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from '@amadda/fetch';
import type { AlarmConfigRequest, ApiResponse } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  try {
    if (req.method === 'POST') {
      const { code, message, data } = await https.post<AlarmConfigRequest, number>(`${process.env.SPRING_API_ROOT}/alarm/subscribe`, token, req.body);
      return res.status(code).json(data);
    }
  } catch (err) {
    Sentry.captureException(err);
    return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
  }

  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'notice/api/alarm/subscribe');
