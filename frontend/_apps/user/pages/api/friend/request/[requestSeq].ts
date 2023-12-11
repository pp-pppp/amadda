import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';
import { ApiResponse } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { requestSeq } = req.query;
  if (req.method === 'POST') {
    //친구신청 수락
    try {
      const { status, message, data } = await https.post<null, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //친구신청 거절
    try {
      const { status, message, data } = await https.put<null, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default wrapApiHandlerWithSentry(auth(handler), 'user/api/friend/request/[requestSeq]');
