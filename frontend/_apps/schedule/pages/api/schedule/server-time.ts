import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, http } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //현재 서버 시간 반환
    try {
      const SPRING_RES = await http.get<string>(
        `${process.env.SPRING_API_ROOT}/schedule/server-time`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
};

export default auth(handler);
