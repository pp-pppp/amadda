import { http } from '@U/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';

const group = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //친구 그룹 만들기
    try {
      const SPRING_RES = await http.post(
        `${process.env.SPRING_API_ROOT}/friend/group`,
        req.body
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  if (req.method === 'PUT') {
    //그룹 친구목록변경/그룹명 변경
    try {
      const SPRING_RES = await http.put(
        `${process.env.SPRING_API_ROOT}/friend/group`,
        req.body
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
export default group;
