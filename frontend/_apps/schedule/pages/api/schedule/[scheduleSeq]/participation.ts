import type { ParticipationListReadResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //멘션 검색
    try {
      const { searchKey, scheduleSeq } = req.query;
      const SPRING_RES = await https.get<ParticipationListReadResponse>(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/participation?searchKey=${searchKey}`,
        token
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
      const { scheduleSeq } = req.query;
      const SPRING_RES = await https.delete(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/participation`,
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
