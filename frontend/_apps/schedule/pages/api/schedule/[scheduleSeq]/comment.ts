import { http } from '@SCH/utils/http';
import { auth } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const { scheduleSeq } = req.query;
  if (req.method === 'POST') {
    //댓글 작성
    try {
      const SPRING_RES = await http.post(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/comment`,
        req.body
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
