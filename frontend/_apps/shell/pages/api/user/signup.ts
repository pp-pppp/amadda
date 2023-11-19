import { http } from '@SH/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserInitRequest } from 'amadda-global-types';

const signup = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //첫 로그인시 사용자 회원가입
    try {
      const SPRING_RES = await http.post<UserInitRequest>(
        `${process.env.SPRING_API_ROOT}/user/signup`,
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
export default signup;
