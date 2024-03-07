import { kv, clientFetch } from '@amadda/fetch';
import cookie from 'cookie';
import type { GetServerSideProps } from 'next';
import { ApiResponse, UserJwtRequest, UserJwtResponse } from '@amadda/global-types';
import SignUp from '@/components/sign-up/sign-up';
import { kakaoAuth } from '@amadda/fetch';
interface KakaoPageProps {
  kakaoId: string;
  imageUrl: string;
  userName: string;
  isInited: boolean;
}
export default function KakaoPage({ ...props }: KakaoPageProps) {
  return <SignUp userInfo={{ ...props }} />;
}

export const getServerSideProps = (async context => {
  const { code } = context.query;

  try {
    const { access_token } = await kakaoAuth.post(
      `https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=${process.env.NEXT_PUBLIC_KAKAO_KEY}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI}&code=${code}`
    ); //카카오 토큰 발급

    const USER_RES = await kakaoAuth.secureGet(`https://kapi.kakao.com/v2/user/me`, access_token); //카카오 유저 정보 조회

    const SpringTokenReq: UserJwtRequest = {
      kakaoId: USER_RES.id,
      imageUrl: USER_RES.properties.profile_image,
    };
    const INIT = await clientFetch.post<UserJwtRequest, UserJwtResponse>(`${process.env.SPRING_API_ROOT}/user/login`, SpringTokenReq); //회원가입이 되었는지 아닌지 확인해 회원가입되었다면 토큰 반환

    const userdata: KakaoPageProps = {
      kakaoId: USER_RES.id,
      imageUrl: USER_RES.properties.profile_image,
      userName: USER_RES.properties.nickname,
      isInited: INIT.isInited,
    };

    if (userdata.isInited) {
      //이미 회원인 경우
      await kv.setToken(INIT.refreshAccessKey, INIT.refreshToken);
      //리프레시 토큰 레디스에 저장

      await kv.setToken(INIT.kakaoId, access_token);
      //카카오 토큰 레디스에 저장

      const { res } = context;
      const COOKIE = cookie.serialize('Auth', INIT.accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV !== 'development',
        path: '/',
        sameSite: 'lax',
        maxAge: 60 * 15,
      });
      res.setHeader('Set-Cookie', COOKIE);
      // 쿠키에 토큰 저장

      // 리다이렉트
      return {
        redirect: {
          destination: `${process.env.NEXT_PUBLIC_SHELL}/calendar`,
          permanent: false,
        },
      };
    }

    //회원이 아닌 경우 회원가입페이지 SSR 진행
    return { props: userdata };
  } catch (err) {
    return err;
  }
}) satisfies GetServerSideProps<KakaoPageProps>;
