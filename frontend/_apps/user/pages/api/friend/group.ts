import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';
import type { ApiResponse, GroupCreateRequest, GroupUpdateRequest } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'POST') {
    //친구 그룹 만들기
    try {
      const { status, message, data } = await https.post<GroupCreateRequest, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/friend/group`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //그룹 친구목록변경/그룹명 변경
    try {
      const { status, message, data } = await https.put<GroupUpdateRequest, ApiResponse<number>>(
        `${process.env.SPRING_API_ROOT}/friend/group`,
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
export default wrapApiHandlerWithSentry(auth(handler), 'user/api/friend/group');
