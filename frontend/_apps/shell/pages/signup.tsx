import { KV } from 'connection';
import cookie from 'cookie';
import type { GetServerSideProps } from 'next';
import { UserJwtRequest, UserJwtResponse } from 'amadda-global-types';
import SignUp from '@SH/components/SignUp/SignUp';
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
    const TOKEN_RES = await fetch(
      `https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=${process.env.NEXT_PUBLIC_KAKAO_KEY}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI}&code=${code}`,
      {
        method: 'POST',
        body: JSON.stringify(null),
        headers: {
          'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
        },
      }
    )
      .then(res => res.json())
      .catch(err => console.log(err));

    const { access_token } = TOKEN_RES;
    const USER_RES = await fetch(`https://kapi.kakao.com/v2/user/me`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${access_token}`,
        'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    })
      .then(res => res.json())
      .catch(err => console.log(err));

    const UserJwtRequest: UserJwtRequest = {
      kakaoId: USER_RES.id,
      imageUrl: USER_RES.properties.profile_image,
    };

    const INIT: UserJwtResponse = await fetch(
      `${process.env.SPRING_API_ROOT}/user/login`,
      {
        method: 'POST',
        body: JSON.stringify(UserJwtRequest),
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
      .then(res => res.json())
      .then(json => json.data)
      .catch(err => console.log(err));

    const { res } = context;
    const COOKIE = cookie.serialize('Auth', INIT.accessToken, {
      httpOnly: true,
      secure: process.env.NODE_ENV !== 'development',
      path: '/',
      sameSite: 'lax',
      maxAge: 60 * 15,
    });

    res.setHeader('Set-Cookie', COOKIE);
    await KV.setRefreshToken(INIT.refreshAccessKey, INIT.refreshToken);

    const userdata: KakaoPageProps = {
      kakaoId: USER_RES.id,
      imageUrl: USER_RES.properties.profile_image,
      userName: USER_RES.properties.nickname,
      isInited: INIT.isInited,
    };

    if (userdata.isInited)
      //이미 회원인 경우 리다이렉트합니다
      return {
        redirect: {
          destination: `${process.env.NEXT_PUBLIC_SHELL}/schedule`,
          permanent: false,
        },
      };

    return { props: userdata };
  } catch (err) {
    return err;
  }
}) satisfies GetServerSideProps<KakaoPageProps>;
