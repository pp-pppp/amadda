import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';
import { ApiResponse, CommentCreateRequest } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq } = req.query;
  if (req.method === 'POST') {
    //댓글 작성
    try {
      const { status, message, data } = await https.post<CommentCreateRequest, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/comment`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'schedule/api/schedule/comment');
