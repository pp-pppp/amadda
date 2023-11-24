import { http } from '@SCH/utils/http';
import type {
  ScheduleCreateResponse,
  ScheduleListReadResponse,
} from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';

const schedule = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //일정 조회 및 검색
    try {
      const { categorySeq, searchKey, unscheduled } = req.query;
      const queryParams = new URLSearchParams();

      if (categorySeq) {
        let result: string = '';
        if (Array.isArray(categorySeq)) result = categorySeq.join(',');
        queryParams.append('category', result);
      }

      if (searchKey) {
        let result: string = '';
        if (Array.isArray(searchKey)) result = searchKey.join(',');
        queryParams.append('searchKey', result);
      }

      if (unscheduled) {
        let result: string = '';
        if (Array.isArray(unscheduled)) result = unscheduled.join(',');
        queryParams.append('unscheduled', result);
      }

      const queryString = queryParams.toString();
      const url =
        queryString.length > 0
          ? `${process.env.SPRING_API_ROOT}/schedule?${queryString}`
          : `${process.env.SPRING_API_ROOT}/schedule`;

      const SPRING_RES = await http.get<ScheduleListReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule${url}`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'POST') {
    //일정 생성
    try {
      const SPRING_RES = await http.post<ScheduleCreateResponse>(
        `${process.env.SPRING_API_ROOT}/schedule`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default schedule;
