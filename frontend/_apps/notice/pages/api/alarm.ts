import type { NextApiRequest, NextApiResponse } from 'next';
import type { AlarmReadResponse, ApiResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    try {
      const { status, message, data } = await https.get<ApiResponse<AlarmReadResponse>>(`${process.env.SPRING_API_ROOT}/alarm`, token);
      return res.status(status).json(data);
    } catch (err) {
      console.log(err);
      return res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default auth(handler);
