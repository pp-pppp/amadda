import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';
import type { ApiResponse, GroupCreateRequest, GroupUpdateRequest } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'POST') {
    //친구 그룹 만들기
    try {
      const { code, message, data } = await https.post<GroupCreateRequest, number>(`${process.env.SPRING_API_ROOT}/friend/group`, token, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'PUT') {
    //그룹 친구목록변경/그룹명 변경
    try {
      const { code, message, data } = await https.put<GroupUpdateRequest, number>(`${process.env.SPRING_API_ROOT}/friend/group`, token, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'user/api/friend/group');
