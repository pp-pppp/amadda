import { http } from '@N/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { AlarmReadResponse } from 'amadda-global-types';

const alarm = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    try {
      const SPRING_RES = await http.get<AlarmReadResponse>(
        `${process.env.SPRING_API_ROOT}/alarm`
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default alarm;
