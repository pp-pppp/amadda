import NextAuth from 'next-auth';

declare module 'next-auth' {
  interface Session {
    user: {
      userSeq: string;
      userName: string;
      userId: string;
      imageUrl: string;
      accessToken: string;
      refreshToken: string;
      refreshAccessKey: string;
      isInited: boolean;
    };
  }
}
