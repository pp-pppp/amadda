import { ApiResponse, CategoryCreateRequest, CategoryCreateResponse, CategoryReadResponse } from '@amadda/global-types';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //해당 사용자의 카테고리 목록 띄워주기
    try {
      const { code, message, data } = await https.get<CategoryReadResponse[]>(`${process.env.SPRING_API_ROOT}/schedule/user/category`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'POST') {
    //카테고리 생성
    try {
      const { code, message, data } = await https.post<CategoryCreateRequest, CategoryCreateResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/user/category`,
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
export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'schedule/api/user/category');
