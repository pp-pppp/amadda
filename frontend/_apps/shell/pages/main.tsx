import { P } from '#/index';
import SignUp from '@/components/SignUp/SignUp';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';

export default function Main() {
  const { data: session } = useSession();

  const [username, setUsername] = useState('');
  const [isInited, setIsInited] = useState(false);

  useEffect(() => {
    session?.user.userName && setUsername(session?.user.userName);
    session?.user.isInited && setIsInited(session?.user.isInited);
  }, [session?.user.isInited]);

  if (isInited) {
    return (
      <>
        <P>접속 유저 : {username} </P>
        <P color="key">Shell(3000)의 메인 화면입니다.</P>
        <P color="key">추후 다른 포트에서 컴포넌트를 임포트할 예정이에요.</P>
      </>
    );
  } else {
    return <SignUp />;
  }
}
