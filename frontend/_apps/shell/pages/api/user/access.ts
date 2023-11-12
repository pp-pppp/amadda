import { http } from '@SH/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserAccessResponse } from 'amadda-global-types';

const access = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //토큰 유효성 검사
    try {
      const SPRING_RES = await http.get<UserAccessResponse>(
        `${process.env.SPRING_API_ROOT}/user/access`
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
export default access;
