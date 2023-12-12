import type { ApiResponse, UserRelationResponse } from 'amadda-global-types';
import { auth, https } from 'connection';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //전체 유저 검색
    try {
      const { searchKey } = req.query;
      const { status, message, data } = await https.get<ApiResponse<UserRelationResponse>>(`${process.env.SPRING_API_ROOT}/user?searchKey=${searchKey}`, token);
      return res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    //회원탈퇴
    try {
      const { status, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/user`, token);
      return res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'user/api/user');
