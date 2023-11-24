import { http } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const friendUserSeq = async (req: NextApiRequest, res: NextApiResponse) => {
  const { friendUserSeq } = req.query;
  if (req.method === 'DELETE') {
    //친구 언팔
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/friend/${friendUserSeq}`
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
export default friendUserSeq;
