import NextAuth, { Session } from 'next-auth';
import 'next-auth';
import KakaoProvider from 'next-auth/providers/kakao';
import { UserJwtRequest, UserJwtResponse } from 'amadda-global-types';
import { http, KV } from 'connection';

export const authOptions = {
  providers: [
    KakaoProvider({
      clientId: process.env.KAKAO_CLIENT_ID!,
      clientSecret: process.env.KAKAO_CLIENT_SECRET!,
    }),
  ],
  callbacks: {
    async session({ session, token }) {
      session.user.kakaoId = token.kakaoId;
      session.user.userName = token.userName;
      session.user.imageUrl = token.imageUrl;
      session.user.accessToken = token.accessToken;
      session.user.isInited = token.isInited;

      return session;
    },

    async jwt({ token, trigger, user, account, session }) {
      try {
        if (account) {
          const UserJwtRequest: UserJwtRequest = {
            kakaoId: user.id,
            imageUrl: user.image,
          };
          console.log(user);
          console.log(UserJwtRequest);
          const INIT = await http
            .post<UserJwtRequest, UserJwtResponse>(
              `${process.env.SPRING_API_ROOT}/user/login`,
              UserJwtRequest
            )
            .then(res => res.data)
            .catch(err => err);

          await KV.setRefreshToken(INIT.refreshAccessKey, INIT.refreshToken);

          token.accessToken = INIT.accessToken;
          token.kakaoId = user.id;
          token.userName = user.name;
          token.imageUrl = user.image;
          token.isInited = INIT.isInited;

          console.log(INIT);
        }

        if (trigger == 'update') {
          token.userName = session?.userName
            ? session?.userName
            : token.userName;
          token.userId = session?.userId ? session?.userId : token.userId;
          token.isInited = session?.isInited
            ? session?.isInited
            : token.isInited;
        }
      } catch (err) {}

      return token;
    },
  },
  pages: {
    signIn: '/',
  },
};

export default NextAuth(authOptions);
