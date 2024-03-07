import type { ApiResponse, ScheduleCreateRequest, ScheduleCreateResponse, ScheduleListReadResponse } from '@amadda/global-types';

import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //일정 조회 및 검색
    try {
      const { categorySeq, searchKey, unscheduled } = req.query;
      const queryParams = new URLSearchParams();

      if (categorySeq) {
        let result: string = '';
        if (Array.isArray(categorySeq)) {
          result = categorySeq.join(',');
          queryParams.append('category', result);
        }
      }

      if (searchKey) {
        let result: string = '';
        if (Array.isArray(searchKey)) {
          result = searchKey.join(',');
          queryParams.append('searchKey', result);
        }
      }

      if (unscheduled) {
        let result: string = '';
        if (Array.isArray(unscheduled)) {
          result = unscheduled.join(',');
          queryParams.append('unscheduled', result);
        }
      }

      const queryString = queryParams.toString();
      const url = queryString.length > 0 ? `${process.env.SPRING_API_ROOT}/schedule?${queryString}` : `${process.env.SPRING_API_ROOT}/schedule`;

      const { code, message, data } = await https.get<ScheduleListReadResponse>(url, token);
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  if (req.method === 'POST') {
    //일정 생성
    try {
      const { code, message, data } = await https.post<ScheduleCreateRequest, ScheduleCreateResponse>(
        `${process.env.SPRING_API_ROOT}/schedule`,
        token,
        req.body
      );
      return res.status(code).json(data);
    } catch (err) {
      Sentry.captureException(err);
      return res.status(err.code || 520).json({ data: err.message || 'unknown server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'schedule/api/schedule');
