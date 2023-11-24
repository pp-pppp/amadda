import { http } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const subscribe = async (req: NextApiRequest, res: NextApiResponse) => {
  try {
    if (req.method === 'POST') {
      const SPRING_RES = await http.post(
        `${process.env.SPRING_API_ROOT}/alarm/subscribe`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    }
  } catch (err) {
    console.log(err);
    res
      .status(err.status || 500)
      .json(err?.data || { data: 'internal server error' });
  }

  res.status(400).json({ data: 'bad request' });
};

export default subscribe;
