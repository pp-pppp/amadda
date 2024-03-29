import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';
import { ApiResponse, CommentCreateRequest } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { schedule_seq } = req.query;
  if (req.method === 'POST') {
    //댓글 작성
    try {
      const { code, message, data } = await https.post<CommentCreateRequest, number>(
        `${process.env.SPRING_API_ROOT}/schedule/${schedule_seq}/comment`,
        token,
        req.body
      );
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'schedule/api/schedule/comment');
