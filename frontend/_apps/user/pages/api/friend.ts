import type { NextApiRequest, NextApiResponse } from 'next';
import type { FriendReadResponse } from 'amadda-global-types';
import { auth, https } from 'connection';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'GET') {
    //친구 목록 검색(친구+그룹)
    try {
      const { searchKey } = req.query;
      const SPRING_RES = await https.get<FriendReadResponse>(
        `${process.env.SPRING_API_ROOT}/friend?searchKey=${searchKey}`,
        token
      );
      res.status(SPRING_RES.status).json(SPRING_RES.data);
    } catch (err) {
      console.log(err);
      res
        .status(err.status || 500)
        .json(err?.data || { data: 'internal server error' });
    }
  }
  res.status(400).json({ data: 'bad request' });
};
export default auth(handler);
