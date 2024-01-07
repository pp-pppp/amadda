import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { friendUserSeq } = req.query;
  if (req.method === 'DELETE') {
    //친구 언팔
    try {
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/friend/${friendUserSeq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'user/api/friend/[friendUserSeq]');
