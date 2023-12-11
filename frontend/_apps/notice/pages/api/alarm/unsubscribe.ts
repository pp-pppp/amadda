import { AlarmConfigRequest, ApiResponse } from 'amadda-global-types';
import { auth, https } from 'connection';
import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  try {
    if (req.method === 'POST') {
      const { status, message, data } = await https.post<AlarmConfigRequest, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/alarm/unsubscribe`,
        token,
        req.body
      );
      res.status(status).json(data);
    }
  } catch (err) {
    console.log(err);
    res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
  }

  res.status(400).json({ data: 'bad request' });
};

export default wrapApiHandlerWithSentry(auth(handler), 'notice/api/alarm/unsubscribe');
