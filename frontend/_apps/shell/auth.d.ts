import NextAuth from 'next-auth';

declare module 'next-auth' {
  interface Session {
    user: {
      id: string;
      name: string;
      image: string;
      accessToken: string;
      refreshToken: string;
      isInited: boolean;
    };
  }
}
