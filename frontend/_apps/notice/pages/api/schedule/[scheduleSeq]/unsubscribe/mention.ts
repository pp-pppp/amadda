import type { NextApiRequest, NextApiResponse } from 'next';

const mention = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    try {
    } catch (err) {
      console.log(err);
    }
  }
  res.status(400).json({ data: 'bad request' });
};

export default mention;
