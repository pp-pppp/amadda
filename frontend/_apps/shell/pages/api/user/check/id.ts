import { http } from '@SH/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserIdCheckResponse } from 'amadda-global-types';

const id = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //아이디만 중복&유효성 검사
    try {
      const SPRING_RES = await http.post<UserIdCheckResponse>(
        `${process.env.SPRING_API_ROOT}/user/check/id`,
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
export default id;
