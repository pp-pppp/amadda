import { http } from 'connection';
import {
  CategoryCreateResponse,
  CategoryReadResponse,
} from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';

const user = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //해당 사용자의 카테고리 목록 띄워주기
    try {
      const SPRING_RES = await http.get<Array<CategoryReadResponse>>(
        `${process.env.SPRING_API_ROOT}/user/category`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'POST') {
    //카테고리 생성
    try {
      const SPRING_RES = await http.post<CategoryCreateResponse>(
        `${process.env.SPRING_API_ROOT}/user/category`,
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
export default user;
