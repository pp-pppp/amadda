import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'POST') {
    try {
    } catch (err) {
      console.log(err);
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default auth(handler);
