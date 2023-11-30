import type { NextApiRequest, NextApiResponse } from 'next';
import type { ApiResponse, UserReadResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //내 유저정보 가져오기
    try {
      const { status, message, data } = await https.get<ApiResponse<UserReadResponse>>(`${process.env.SPRING_API_ROOT}/user/my`, token);
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
};
export default auth(handler);
