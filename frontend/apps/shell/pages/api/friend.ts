import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, FriendReadResponse } from '@amadda/global-types';
import { auth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //친구 목록 검색(친구+그룹)
    try {
      const { searchKey } = req.query;
      const { code, message, data } = await https.get<FriendReadResponse>(`${process.env.SPRING_API_ROOT}/friend?searchKey=${searchKey}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'user/api/friend');
