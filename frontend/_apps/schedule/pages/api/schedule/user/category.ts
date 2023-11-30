import { ApiResponse, CategoryCreateRequest, CategoryCreateResponse, CategoryReadResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //해당 사용자의 카테고리 목록 띄워주기
    try {
      const { status, message, data } = await https.get<ApiResponse<CategoryReadResponse[]>>(`${process.env.SPRING_API_ROOT}/schedule/user/category`, token);
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'POST') {
    //카테고리 생성
    try {
      const { status, message, data } = await https.post<CategoryCreateRequest, ApiResponse<CategoryCreateResponse>>(
        `${process.env.SPRING_API_ROOT}/schedule/user/category`,
        token,
        req.body
      );
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default auth(handler);
