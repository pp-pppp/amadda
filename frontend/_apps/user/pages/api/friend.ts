import { http } from '@U/utils/http';
import type { NextApiRequest, NextApiResponse } from 'next';
import type { FriendReadResponse } from 'amadda-global-types';

const friend = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'GET') {
    //친구 목록 검색(친구+그룹)
    try {
      const { searchKey } = req.query;
      const SPRING_RES = await http.get<FriendReadResponse>(
        `${process.env.SPRING_API_ROOT}/friend?searchKey=${searchKey}`
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
export default friend;
