import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, UserRelationResponse } from '@amadda/global-types';
import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //유저 정보(호버용)
    try {
      const { user_seq } = req.query;
      const { code, message, data } = await https.get<UserRelationResponse>(`${process.env.SPRING_API_ROOT}/user/${user_seq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'user/api/user/[user_seq]');
