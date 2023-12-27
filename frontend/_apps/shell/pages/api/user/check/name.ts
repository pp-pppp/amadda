import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, UserNameCheckResponse } from 'amadda-global-types';
import { http } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //닉네임 유효성 검사
    try {
      const { code, message, data } = await http.post<string, UserNameCheckResponse>(`${process.env.SPRING_API_ROOT}/user/check/name`, req.body);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};
export default Sentry.wrapApiHandlerWithSentry(handler, 'shell/api/check/name');
