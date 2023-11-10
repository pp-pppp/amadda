import { http } from '@/utils/http';
import type { ParticipationListReadResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';

const participation = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //멘션 검색
    try {
      const { searchKey } = req.query;
      const SPRING_RES = await http.get<ParticipationListReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/{scheduleSeq}/participation?searchKey=${searchKey}`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    //참가 정보 삭제
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/schedule/{scheduleSeq}/participation`
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

export default participation;
