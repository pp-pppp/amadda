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
      // spring에서 AT 가져오기
      const UserJwtRequestBody: AuthRequest = {
        userSeq: token.id,
        imageUrl: token.image,
      };

      // const data = await http.post<AuthRequest, AuthResponse>(
      //   'http://localhost:8080/user/login',
      //   UserJwtRequestBody
      // );

      // mock
      const mock = {
        accessToken: 'atatatat',
        refreshToken: 'rtrtrtrt',
        refreshAccessKey: 'rakrak',
        isInited: true,
      };

      session.user.id = token.id;
      session.user.name = token.name;
      session.user.image = token.image;
      session.user.accessToken = mock.accessToken;
      session.user.refreshToken = mock.refreshToken;
      session.user.isInited = mock.isInited;

      return session;
    },

    async jwt({ token, user, account }) {
      if (account) {
        token.accessToken = account.access_token;
        token.id = user.id;
        token.name = user.name;
        token.image = user.image;
      }
      return token;
    },
  },
  pages: {
    signIn: '/',
  },
};

export default NextAuth(authOptions);
