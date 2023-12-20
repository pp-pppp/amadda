import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';
import { ApiResponse, FriendRequestRequest } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'POST') {
    //친구신청 보내기
    try {
      const { status, message, data } = await https.post<FriendRequestRequest, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/friend/request`,
        token,
        req.body
      );
      return res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'user/api/friend/request');
