import type { NextApiRequest, NextApiResponse } from 'next';
import type { AlarmReadResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { alarmSeq } = req.query;
  if (req.method === 'POST') {
    try {
      const SPRING_RES = await https.get<AlarmReadResponse>(
        `${process.env.SPRING_API_ROOT}/alarm/${alarmSeq}`,
        token
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

export default auth(handler);
