import { http } from '@/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';

const serverTime = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //현재 서버 시간 반환
    try {
      const SPRING_RES = await http.get<string>(
        `${process.env.SPRING_API_ROOT}/schedule/serverTime`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
};

export default serverTime;
