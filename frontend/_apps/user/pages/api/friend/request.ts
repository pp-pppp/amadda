import { http } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const request = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //친구신청 보내기
    try {
      const SPRING_RES = await http.post(
        `${process.env.SPRING_API_ROOT}/friend/request`,
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
export default request;
