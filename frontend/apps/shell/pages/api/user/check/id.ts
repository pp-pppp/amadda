import { http } from '@amadda/fetch';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, UserIdCheckResponse } from '@amadda/global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //아이디만 중복&유효성 검사
    try {
      const { code, message, data } = await http.post<string, UserIdCheckResponse>(`${process.env.SPRING_API_ROOT}/user/check/id`, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(handler, 'shell/api/check/id');
