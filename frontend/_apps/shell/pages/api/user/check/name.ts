import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, UserNameCheckResponse } from 'amadda-global-types';
import { http } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //닉네임 유효성 검사
    try {
      const { status, message, data } = await http.post<string, ApiResponse<UserNameCheckResponse>>(`${process.env.SPRING_API_ROOT}/user/check/name`, req.body);
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default wrapApiHandlerWithSentry(handler, 'shell/api/check/name');
