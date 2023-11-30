import type { NextApiRequest, NextApiResponse } from 'next';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  const { groupSeq } = req.query;
  if (req.method === 'DELETE') {
    //그룹 삭제
    try {
      const { status, message, data } = await https.delete(`${process.env.SPRING_API_ROOT}/friend/group/${groupSeq}`, token);
      res.status(status).json(data);
    } catch (err) {
      res.status(err.status || 500).json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default auth(handler);
