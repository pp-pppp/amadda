import { P } from '#/index';
import { useSession } from 'next-auth/react';

export default function Main() {
  const { data } = useSession();

  const username = data?.user.name || '';
  return (
    <>
      <P>접속 유저 : {username} </P>
      <P color="key">Shell(3000)의 메인 화면입니다.</P>
      <P color="key">추후 다른 포트에서 컴포넌트를 임포트할 예정이에요.</P>
    </>
  );
}
