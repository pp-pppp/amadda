import { UserAccessResponse, UserJwtResponse } from 'amadda-global-types';
import type { NextApiRequest, NextApiResponse } from 'next';
import { KV } from './kv';

type API_HANDLER = (request: NextApiRequest, response: NextApiResponse) => Promise<NextApiResponse | void>;

export function auth(fn: API_HANDLER): API_HANDLER {
  return async function (req: NextApiRequest, res: NextApiResponse) {
    //auth는 api 핸들러를 받아 인증 처리를 한 API 결과를 리턴합니다.
    const CLIENT_TOKEN = req.cookies.Auth || '';
    //클라이언트에서 보낸 쿠키의 access token을 가져옵니다.

    try {
      const ACCESS_TOKEN = await VERIFIED_TOKEN(CLIENT_TOKEN);
      //현재 토큰이 유효하면 현재 토큰, 아니라면 새 토큰이 들어옵니다.
      req.headers.authorization = `Bearer ${ACCESS_TOKEN}`;
      await fn(req, res);
      return res.setHeader('Set-Cookie', `Auth=${ACCESS_TOKEN}; Max-Age=900; HttpOnly; SameSite=Lax;`);
    } catch (err) {
      //모든 종류의 에러는 로그아웃으로 리다이렉트시킵니다.
      return res.redirect(`${process.env.NEXT_PUBLIC_SHELL}/signout`);
    }
  };
}

async function VERIFY(type: 'access' | 'refresh', token: string): Promise<any> {
  const response = await fetch(`${process.env.SPRING_API_ROOT as string}/user/${type}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });
  return await response.json();
}

async function VERIFIED_TOKEN(accessToken: string) {
  const inspect_AT: UserAccessResponse = await VERIFY('access', accessToken);
  //엑세스 토큰에 대한 검증 요청을 보냅니다.
  if (inspect_AT.isBroken || !inspect_AT) throw new Error('Broken Token');
  //토큰이 손상되었다면 exception을 던집니다.
  if (inspect_AT.isExpired) {
    //토큰 시간이 만료된 것이라면 리프레시 토큰을 확인할 수 있는 key가 제공됩니다.
    const RT = (await KV.getRefreshToken(inspect_AT.refreshAccessKey)) || '';
    //리프레시 토큰은 레디스(vercel KV)에 저장되어 있습니다. 레디스에서 key를 통해 토큰을 꺼냅니다.
    const inspect_RT: UserJwtResponse = await VERIFY('refresh', RT);
    //리프레시 토큰에 대한 검증 요청을 보냅니다.
    KV.setRefreshToken(inspect_RT.refreshAccessKey, inspect_RT.refreshToken);
    //검증 응답이 잘 왔다면 새 리프레시 토큰을 레디스(vercel KV)에 저장하고,
    return inspect_RT.accessToken;
    //새로운 액세스 토큰을 넣어서 리턴합니다.
  }
  return accessToken;
  //기존 엑세스 토큰에 문제가 없었다면 그대로 리턴합니다.
}
