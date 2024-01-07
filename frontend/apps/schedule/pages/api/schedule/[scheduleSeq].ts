import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { ScheduleUpdateRequest, type ApiResponse, type ScheduleDetailReadResponse, ScheduleUpdateResponse } from '@amadda/global-types';

import { auth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq } = req.query;
  if (req.method === 'GET') {
    //사용자 일정 상세 보기 (댓글 포함)
    try {
      const { code, message, data } = await https.get<ScheduleDetailReadResponse>(`${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'PUT') {
    //일정 수정하기
    try {
      const { code, message, data } = await https.put<ScheduleUpdateRequest, ScheduleUpdateResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
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
    try {
      const { code, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), '');
