import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { requestSeq } = req.query;
  if (req.method === 'POST') {
    //친구신청 수락
    try {
      const { code, message, data } = await https.post<null, number>(`${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`, token, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'PUT') {
    //친구신청 거절
    try {
      const { code, message, data } = await https.put<null, number>(`${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`, token, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'user/api/friend/request/[requestSeq]');
