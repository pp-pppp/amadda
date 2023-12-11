import type { ApiResponse, ScheduleCreateRequest, ScheduleCreateResponse, ScheduleListReadResponse } from 'amadda-global-types';

import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { auth, https } from 'connection';

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

      const { status, message, data } = await https.get<ApiResponse<ScheduleListReadResponse>>(url, token);
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'POST') {
    //일정 생성
    try {
      const { status, message, data } = await https.post<ScheduleCreateRequest, ApiResponse<ScheduleCreateResponse>>(
        `${process.env.SPRING_API_ROOT}/schedule`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default wrapApiHandlerWithSentry(auth(handler), 'schedule/api/schedule');
