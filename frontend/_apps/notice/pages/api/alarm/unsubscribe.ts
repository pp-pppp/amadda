import { auth, https } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  try {
    if (req.method === 'POST') {
      const SPRING_RES = await https.post(
        `${process.env.SPRING_API_ROOT}/alarm/unsubscribe`,
        token,
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

export default auth(handler);
