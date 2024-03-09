import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { group_seq } = req.query;
  if (req.method === 'DELETE') {
    //그룹 삭제
    try {
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/friend/group/${group_seq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'user/api/friend/group/[group_seq]');
