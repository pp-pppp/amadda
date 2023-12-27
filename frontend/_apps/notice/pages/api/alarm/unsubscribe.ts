import { AlarmConfigRequest, ApiResponse } from 'amadda-global-types';
import { auth, https } from 'connection';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  try {
    if (req.method === 'POST') {
      const { code, message, data } = await https.post<AlarmConfigRequest, number>(`${process.env.SPRING_API_ROOT}/alarm/unsubscribe`, token, req.body);
      return res.status(code).json(data);
    }
  } catch (err) {
    Sentry.captureException(err);
    return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
  }

  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'notice/api/alarm/unsubscribe');
