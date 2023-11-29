import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, http } from 'connection';
import type { ApiResponse } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //현재 서버 시간 반환
    try {
      const { status, message, data } = await http.get<ApiResponse<string>>(
        `${process.env.SPRING_API_ROOT}/schedule/server-time`
      );
      res.status(status).json(data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
};

export default auth(handler);
