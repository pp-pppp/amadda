import { http } from '@U/utils/http';
import { CategoryUpdateResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';

const categorySeq = async (req: NextApiRequest, res: NextApiResponse) => {
  const { categorySeq } = req.query;
  if (req.method === 'PUT') {
    //카테고리 수정 (이름/색깔)
    try {
      const SPRING_RES = await http.put<CategoryUpdateResponse>(
        `${process.env.SPRING_API_ROOT}/user/category/${categorySeq}`,
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
  if (req.method === 'DELETE') {
    //카테고리 삭제
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/user/category/${categorySeq}`
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
export default categorySeq;
