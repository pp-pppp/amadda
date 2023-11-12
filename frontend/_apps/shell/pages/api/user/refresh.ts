import { http } from '@SH/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserJwtResponse } from 'amadda-global-types';

const refresh = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //Refresh 주고 새 토큰 세개 받기
    try {
      const SPRING_RES = await http.post<UserJwtResponse>(
        `${process.env.SPRING_API_ROOT}/user/refresh`,
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
export default refresh;
