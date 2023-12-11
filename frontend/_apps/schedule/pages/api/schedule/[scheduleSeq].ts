import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { ScheduleUpdateRequest, type ApiResponse, type ScheduleDetailReadResponse, ScheduleUpdateResponse } from 'amadda-global-types';

import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq } = req.query;
  if (req.method === 'GET') {
    //사용자 일정 상세 보기 (댓글 포함)
    try {
      const { status, message, data } = await https.get<ApiResponse<ScheduleDetailReadResponse>>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        token
      );
      res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //일정 수정하기
    try {
      const { status, message, data } = await https.put<ScheduleUpdateRequest, ApiResponse<ScheduleUpdateResponse>>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    try {
      const { status, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`, token);
      res.status(status).json(data);
    } catch (err) {
      Sentry.captureException(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), '');
