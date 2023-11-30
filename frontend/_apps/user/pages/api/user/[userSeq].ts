import type { NextApiRequest, NextApiResponse } from 'next';
import type { ApiResponse, UserRelationResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //유저 정보(호버용)
    try {
      const { userSeq } = req.query;
      const { status, message, data } = await https.get<ApiResponse<UserRelationResponse>>(`${process.env.SPRING_API_ROOT}/user/${userSeq}`, token);
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default auth(handler);
