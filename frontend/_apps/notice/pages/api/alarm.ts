import { http } from '@N/utils/http';
import { auth } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { AlarmReadResponse } from 'amadda-global-types';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    try {
      const SPRING_RES = await http.get<AlarmReadResponse>(
        `${process.env.SPRING_API_ROOT}/alarm`
      );
      return res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      return res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default auth(handler);
