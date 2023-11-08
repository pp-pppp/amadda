import { http } from '#/index';
import axios from 'axios';
import NextAuth, { Session } from 'next-auth';
import 'next-auth';
import KakaoProvider from 'next-auth/providers/kakao';

export interface AuthRequest {
  userSeq: string;
  imageUrl: string;
}

export interface AuthResponse {
  accessToken: String;
  refreshToken: String;
  refreshAccessKey: String;
  isInited: boolean;
}

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
      session.user.refreshToken = token.refreshToken;
      session.user.refreshAccessKey = token.refreshAccessKey;
      session.user.isInited = token.isInited;

      return session;
    },

    async jwt({ token, trigger, user, account, session }) {
      if (account) {
        const UserJwtRequest: AuthRequest = {
          userSeq: token.id,
          imageUrl: token.image,
        };

        // const data = await http.post<AuthRequest, AuthResponse>(
        //   `${process.env.SPRING_API_ROOT}/user/login`,
        //   UserJwtRequest
        // );

        // mock
        const data: AuthResponse = {
          accessToken: 'at',
          refreshAccessKey: 'rak',
          refreshToken: 'rt',
          isInited: false,
        };
        console.log(data);

        token.accessToken = data.accessToken;
        token.refreshToken = data.refreshToken;
        token.refreshAccessKey = data.refreshAccessKey;
        token.userSeq = user.id;
        token.userName = user.name;
        token.imageUrl = user.image;
        token.isInited = data.isInited;
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
