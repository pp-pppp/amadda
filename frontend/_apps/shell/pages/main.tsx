import { P } from 'external-temporal';
import SignUp from '@SH/components/SignUp/SignUp';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import useSWR from 'swr';
import { useRouter } from 'next/router';
import { http } from 'connection';
import { UserReadResponse } from 'amadda-global-types';

export default function Main() {
  const router = useRouter();
  const { data: session } = useSession();

  const [username, setUsername] = useState('');
  const [isInited, setIsInited] = useState(false);

  // const fetcher = async () => {
  //   const res = await axios.get(
  //     `${process.env.NEXT_PUBLIC_SHELL}/api/auth/init`,
  //     {
  //       headers: {
  //         init: `${session && session.user.accessToken}`,
  //       },
  //     }
  //   );
  //   return res;
  // };

  // const { data, error } = useSWR('/api/auth/init', fetcher);
  useEffect(() => {
    session?.user.userName && setUsername(session?.user.userName);
    session?.user.isInited && setIsInited(session?.user.isInited);
  }, [session?.user.isInited]);

  // useEffect(() => {
  //   http
  //     .get<UserReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/user/my`)
  //     .then(res => console.log(res));
  // }, [isInited]);

  if (isInited) {
    return (
      <>
        {/* <P>접속 유저 : {currUser?.userName} </P> */}{' '}
        {/* <P>{data && data.status}</P> */}
        <P color="key">Shell(3000)의 메 인 화면입니다.</P>
        <P color="key">추후 다른 포트에서 컴포넌트를 임포트할 예정이에요.</P>
      </>
    );
  } else {
    return <SignUp />;
  }
}
