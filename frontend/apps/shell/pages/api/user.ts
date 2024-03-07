import type { ApiResponse, UserRelationResponse } from '@amadda/global-types';
import { withAuth, https } from '@amadda/fetch';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //전체 유저 검색
    try {
      const { searchKey } = req.query;
      const { code, message, data } = await https.get<UserRelationResponse>(`${process.env.SPRING_API_ROOT}/user?searchKey=${searchKey}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'DELETE') {
    //회원탈퇴
    try {
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/user`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'user/api/user');
