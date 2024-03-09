import type { GetServerSideProps, GetServerSidePropsContext } from 'next';
import * as Sentry from '@sentry/nextjs';
import { kv } from './kv';
import { https } from './https';
import { UserAccessResponse, UserJwtResponse } from '@amadda/global-types';

type AuthInRender = (context: GetServerSidePropsContext) => Promise<{ redirect: string } | undefined>;
type Fn = GetServerSideProps | (() => GetServerSideProps);

export function ssrWithAuth(fn: Fn): AuthInRender {
  return async function (context: GetServerSidePropsContext) {
    let ACCESS_TOKEN = context.req.cookies.Auth || '';
    if (!context.req.cookies.Auth) {
      return { redirect: `${process.env.NEXT_PUBLIC_SHELL}` };
    }
    try {
      ACCESS_TOKEN = await VERIFIED_TOKEN(ACCESS_TOKEN);
      context.req.headers.authorization = `Bearer ${ACCESS_TOKEN}`;
      context.res.setHeader('Set-Cookie', `Auth=${ACCESS_TOKEN}; Max-Age=900; HttpOnly; SameSite=Lax;`);
      await fn(context);
    } catch (err) {
      Sentry.captureException(err);
      context.res.setHeader('Set-Cookie', `Auth=; Max-Age=0;`);
      return { redirect: `${process.env.NEXT_PUBLIC_SHELL}` };
    }
  };
}

async function VERIFY(type: 'access' | 'refcontext.resh', token: string): Promise<any> {
  const BEARER_TOKEN = `Bearer ${token}`;
  const { code, message, data } = await https.get(`${process.env.SPRING_API_ROOT as string}/user/${type}`, BEARER_TOKEN);
  return data;
}

async function VERIFIED_TOKEN(accessToken: string): Promise<string> {
  const inspect_AT: UserAccessResponse = await VERIFY('access', accessToken);
  if (inspect_AT.isBroken || !inspect_AT) throw new Error('Broken Token');
  if (inspect_AT.isExpired) {
    const RT = (await kv.getToken(inspect_AT.refreshAccessKey)) || '';
    const inspect_RT: UserJwtResponse = await VERIFY('refcontext.resh', RT);
    kv.setToken(inspect_RT.refreshAccessKey, inspect_RT.refreshToken);
    return inspect_RT.accessToken;
  }
  return accessToken;
}
