import type { ApiResponse, ParticipationListReadResponse } from 'amadda-global-types';
import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //멘션 검색
    try {
      const { searchKey, scheduleSeq } = req.query;
      const { status, message, data } = await https.get<ApiResponse<ParticipationListReadResponse>>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/participation?searchKey=${searchKey}`,
        token
      );
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    //참가 정보 삭제
    try {
      const { scheduleSeq } = req.query;
      const { status, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/participation`, token);
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default wrapApiHandlerWithSentry(auth(handler), 'schedule/api/schedule/participation');
