import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq, commentSeq } = req.query;
  if (req.method === 'DELETE') {
    //댓글 삭제
    try {
      const { status, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/comment/${commentSeq}`, token);
      return res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'schedule/api/schedule/comment/[commentSeq]');
