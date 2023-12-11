import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import type { ApiResponse, AlarmReadResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { alarmSeq } = req.query;
  if (req.method === 'POST') {
    try {
      const { status, message, data } = await https.get<ApiResponse<AlarmReadResponse>>(`${process.env.SPRING_API_ROOT}/alarm/${alarmSeq}`, token);
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default wrapApiHandlerWithSentry(auth(handler), 'notice/api/alarm/[alarmSeq]');
