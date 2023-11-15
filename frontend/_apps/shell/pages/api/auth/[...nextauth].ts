import { http } from 'connection';
import axios from 'axios';
import { REDIS } from 'connection';
import NextAuth, { Session } from 'next-auth';
import 'next-auth';
import KakaoProvider from 'next-auth/providers/kakao';
import { UserJwtRequest, UserJwtResponse } from 'amadda-global-types';

export const authOptions = {
  providers: [
    KakaoProvider({
      clientId: process.env.KAKAO_CLIENT_ID!,
      clientSecret: process.env.KAKAO_CLIENT_SECRET!,
    }),
  ],
  callbacks: {
    async session({ session, token }) {
      session.user.userSeq = token.userSeq;
      session.user.userName = token.userName;
      session.user.userId = token.userId;
      session.user.imageUrl = token.imageUrl;
      session.user.accessToken = token.accessToken;
      session.user.isInited = token.isInited;

      return session;
    },

    async jwt({ token, trigger, user, account, session }) {
      if (account) {
        const UserJwtRequest: UserJwtRequest = {
          userSeq: token.id,
          imageUrl: token.image,
        };

        const INIT = await http
          .post<UserJwtRequest, UserJwtResponse>(
            `${process.env.SPRING_API_ROOT}/user/login`,
            UserJwtRequest
          )
          .then(res => res.data)
          .catch(err => err);

        await REDIS.setRefreshToken(INIT.refreshAccessKey, INIT.refreshToken);

        console.log(INIT);

        token.accessToken = INIT.accessToken;
        token.userSeq = user.id;
        token.userName = user.name;
        token.imageUrl = user.image;
        token.isInited = INIT.isInited;
      }

      if (trigger == 'update') {
        token.userName = session?.userName ? session?.userName : token.userName;
        token.userId = session?.userId ? session?.userId : token.userId;
        token.isInited = session?.isInited ? session?.isInited : token.isInited;
      }

      return token;
    },
  },
  pages: {
    signIn: '/',
  },
};

export default NextAuth(authOptions);
