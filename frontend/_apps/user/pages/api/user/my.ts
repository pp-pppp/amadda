import { http } from '@U/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserReadResponse } from 'amadda-global-types';

const my = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //내 유저정보 가져오기
    try {
      const SPRING_RES = await http.get<UserReadResponse>(
        `${process.env.SPRING_API_ROOT}/user/my`
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
export default my;
