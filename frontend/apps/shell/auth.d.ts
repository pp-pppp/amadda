import NextAuth from 'next-auth';

declare module 'next-auth' {
  interface Session {
    user: {
      kakaoId: string;
      userName: string;
      userId: string;
      imageUrl: string;
      accessToken: string;
      isInited: boolean;
    };
  }
}
