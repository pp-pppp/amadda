import { http } from '@SH/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserNameCheckResponse } from 'amadda-global-types';

const name = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //닉네임 유효성 검사
    try {
      const SPRING_RES = await http.post<UserNameCheckResponse>(
        `${process.env.SPRING_API_ROOT}/user/check/name`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default name;
