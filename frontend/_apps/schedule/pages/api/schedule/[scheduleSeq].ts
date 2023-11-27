import type { NextApiRequest, NextApiResponse } from 'next';
import type { ScheduleDetailReadResponse } from 'amadda-global-types';

import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq } = req.query;
  if (req.method === 'GET') {
    //사용자 일정 상세 보기 (댓글 포함)
    try {
      const SPRING_RES = await https.get<ScheduleDetailReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        token
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //일정 수정하기
    try {
      const SPRING_RES = await https.put(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        token,
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
  if (req.method === 'DELETE') {
    try {
      const SPRING_RES = await https.delete(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}`,
        token
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

export default auth(handler);
