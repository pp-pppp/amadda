import type { ApiResponse, ParticipationListReadResponse } from '@amadda/global-types';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //멘션 검색
    try {
      const { searchKey, schedule_seq } = req.query;
      const { code, message, data } = await https.get<ParticipationListReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/${schedule_seq}/participation?searchKey=${searchKey}`,
        token
      );
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'DELETE') {
    //참가 정보 삭제
    try {
      const { schedule_seq } = req.query;
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/${schedule_seq}/participation`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'schedule/api/schedule/participation');
