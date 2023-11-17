import { P } from 'external-temporal';
import SignUp from '@SH/components/SignUp/SignUp';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import useSWR from 'swr';
import { useRouter } from 'next/router';

export default function Main() {
  const router = useRouter();
  const { data: session } = useSession();

  const [username, setUsername] = useState('');
  const [isInited, setIsInited] = useState(false);

  const fetcher = async () => {
    const res = await axios.get(
      `${process.env.NEXT_PUBLIC_SHELL}/api/auth/init`,
      {
        headers: {
          init: `${session && session.user.accessToken}`,
        },
      }
    );
    return res;
  };

  useEffect(() => {
    session?.user.userName && setUsername(session?.user.userName);
    session?.user.isInited && setIsInited(session?.user.isInited);
  }, [session?.user.isInited]);

  if (isInited) {
    return (
      <>
        <P>접속 유저 : {username} </P>
        {/* <P>{data ? data.status : 'ERR'}</P> */}
        <P color="key">Shell(3000)의 메인 화면입니다.</P>
        <P color="key">추후 다른 포트에서 컴포넌트를 임포트할 예정이에요.</P>
      </>
    );
  } else {
    return <SignUp />;
  }
}
