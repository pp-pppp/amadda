import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { scheduleSeq } = req.query;
  if (req.method === 'POST') {
    //댓글 작성
    try {
      const SPRING_RES = await https.post(
        `${process.env.SPRING_API_ROOT}/schedule/${scheduleSeq}/comment`,
        token,
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
