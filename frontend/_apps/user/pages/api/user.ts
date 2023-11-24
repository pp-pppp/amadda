import { http } from '@U/utils/http';
import { UserRelationResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';

const user = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //전체 유저 검색
    try {
      const { searchKey } = req.query;
      const SPRING_RES = await http.get<UserRelationResponse>(
        `${process.env.SPRING_API_ROOT}/user?searchKey=${searchKey}`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'DELETE') {
    //회원탈퇴
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/user`
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
export default user;
