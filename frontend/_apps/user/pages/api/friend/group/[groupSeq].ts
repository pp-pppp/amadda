import { http } from 'connection';
import type { NextApiRequest, NextApiResponse } from 'next';

const groupSeq = async (req: NextApiRequest, res: NextApiResponse) => {
  const { groupSeq } = req.query;
  if (req.method === 'DELETE') {
    //그룹 삭제
    try {
      const SPRING_RES = await http.delete(
        `${process.env.SPRING_API_ROOT}/friend/group/${groupSeq}`
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
export default groupSeq;
