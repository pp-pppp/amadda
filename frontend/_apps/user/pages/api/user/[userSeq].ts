import { http } from '@U/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserRelationResponse } from 'amadda-global-types';

const userSeq = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //유저 정보(호버용)
    try {
      const { userSeq } = req.query;
      const SPRING_RES = await http.get<UserRelationResponse>(
        `${process.env.SPRING_API_ROOT}/user/${userSeq}`
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
export default userSeq;
