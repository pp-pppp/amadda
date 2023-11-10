import type { NextApiRequest, NextApiResponse } from 'next';

const update = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    try {
    } catch (err) {}
  }
  res.status(400).json({ data: 'bad request' });
};

export default update;
