import type { NextApiRequest, NextApiResponse } from 'next';
import type { UserInitRequest, UserJwtResponse } from 'amadda-global-types';
import cookie from 'cookie';
import { KV, http } from 'connection';

const signup = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method === 'POST') {
    //첫 로그인시 사용자 회원가입
    try {
      const SPRING_RES = await http.post<UserInitRequest, UserJwtResponse>(
        `${process.env.SPRING_API_ROOT}/user/signup`,
        req.body
      );

      const COOKIE = cookie.serialize('Auth', SPRING_RES.data.accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV !== 'development',
        path: '/',
        sameSite: 'lax',
        maxAge: 60 * 15,
      });

      res.setHeader('Set-Cookie', COOKIE);
      await KV.setRefreshToken(
        SPRING_RES.data.refreshAccessKey,
        SPRING_RES.data.refreshToken
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
export default signup;
