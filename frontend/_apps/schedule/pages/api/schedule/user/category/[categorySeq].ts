import type { ApiResponse, CategoryCreateRequest, CategoryUpdateResponse } from 'amadda-global-types';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { categorySeq } = req.query;
  if (req.method === 'PUT') {
    //카테고리 수정 (이름/색깔)
    try {
      const { code, message, data } = await https.put<CategoryCreateRequest, CategoryUpdateResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/user/category/${categorySeq}`,
        token,
        req.body
      );
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'DELETE') {
    //카테고리 삭제
    try {
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/user/category/${categorySeq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'schedule/api/user/category/[categorySeq]');
