import { http } from '@U/utils/http';
import { auth } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const { requestSeq } = req.query;
  if (req.method === 'POST') {
    //친구신청 수락
    try {
      const SPRING_RES = await http.post(
        `${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //친구신청 거절
    try {
      const SPRING_RES = await http.put(
        `${process.env.SPRING_API_ROOT}/friend/request/${requestSeq}`,
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
