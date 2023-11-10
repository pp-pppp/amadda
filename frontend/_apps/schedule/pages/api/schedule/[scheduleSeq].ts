import { http } from '@/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { ScheduleDetailReadResponse } from 'amadda-global-types';
const scheduleSeq = async (req: NextApiRequest, res: NextApiResponse) => {
  const { scheduleSeq } = req.query;
  if (req.method === 'GET') {
    //사용자 일정 상세 보기 (댓글 포함)
    try {
      const SPRING_RES = await http.get<ScheduleDetailReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //일정 수정하기
    try {
      const SPRING_RES = await http.put(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default scheduleSeq;
