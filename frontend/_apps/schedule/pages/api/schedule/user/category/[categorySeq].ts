import type {
  ApiResponse,
  CategoryCreateRequest,
  CategoryUpdateResponse,
} from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { categorySeq } = req.query;
  if (req.method === 'PUT') {
    //카테고리 수정 (이름/색깔)
    try {
      const { status, message, data } = await https.put<
        CategoryCreateRequest,
        ApiResponse<CategoryUpdateResponse>
      >(
        `${process.env.SPRING_API_ROOT}/user/category/${categorySeq}`,
        token,
        req.body
      );
      res.status(status).json(data);
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
      const { status, message, data } = await https.delete(
        `${process.env.SPRING_API_ROOT}/user/category/${categorySeq}`,
        token
      );
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default auth(handler);