import React, { useEffect, useState } from 'react';
import { Btn, Flex, H1, Input, Label, P, Spacing, http } from '#/index';
import { IndexLayout } from '@/layout/IndexLayout';
import { useSession } from 'next-auth/react';
import { Profile } from '../../../user/components/Profile/Profile';
import useNameValidator from '@/hooks/useNameValidator';
import useIdValidator from '@/hooks/useIdValidator';
import SIGNUP_TEXT from '@/constants/SIGNUP_TEXT';

export interface UserInitRequest {
  userSeq: string;
  imageUrl: string;
  nickName: string;
  userId: string;
}

export default function SignUp() {
  const { data: session, update } = useSession();
  const userSeq = session?.user.userId || '';
  const imageUrl = session?.user.imageUrl || '';
  const userName = session?.user.userName || '';

  const { nickname, setNickname, isNameEmpty, isNameValid } =
    useNameValidator(userName);
  const { userId, setUserId, isIdEmpty, isDuplicated, isIdValid } =
    useIdValidator('');

  const [btnDisable, setBtnDisable] = useState(true);

  useEffect(() => {
    setNickname(userName);
  }, [session]);

  const checkAllValid = () => {
    if (!isDuplicated && !isNameEmpty && !isIdEmpty && isIdValid && isNameValid)
      return true;
    return false;
  };

  const btnControl = () => {
    if (checkAllValid()) setBtnDisable(false);
    else setBtnDisable(true);
  };

  // userName onChange
  const userNameOnChange = (value: string) => {
    setNickname(value);
    btnControl();
  };

  // userId onChagnge
  const userIdOnChange = (value: string) => {
    setUserId(value);
    btnControl();
  };

  // siginup
  const amaddaSignUp = async () => {
    if (checkAllValid()) {
      await update({ userName: nickname, userId: userId, isInited: true });

      const UserInitRequest: UserInitRequest = {
        userSeq: userSeq,
        imageUrl: imageUrl,
        nickName: nickname,
        userId: userId,
      };

      // await http.post<UserInitRequest>(
      //   `${process.env.SPRING_API_ROOT}/user/signup`,
      //   UserInitRequest
      // );
    }
  };

  const nicknameCaption = () => {
    return isNameEmpty ? (
      <P type="caption" color="warn">
        {SIGNUP_TEXT.NICKNAME_EMPTY}
      </P>
    ) : (
      <P type="caption" color="grey">
        {SIGNUP_TEXT.NICKNAME_DESC}
      </P>
    );
  };

  const idCaption = () => {
    return isIdEmpty ? (
      <P type="caption" color="warn">
        {SIGNUP_TEXT.ID_EMPTY}
      </P>
    ) : (
      <P type="caption" color="grey">
        {SIGNUP_TEXT.ID_DESC}
      </P>
    );
  };

  return (
    <IndexLayout>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H1>A M A D D A</H1>
        <Spacing size="2" />
        <Profile src={imageUrl} alt="profile image" size="large" />
      </Flex>
      <Spacing size="2" />
      <Label htmlFor="getNickname">{SIGNUP_TEXT.NICKNAME}</Label>
      <Spacing size="0.25" />
      <Spacing size="0.25" />
      <Input
        type="text"
        id="getNickname"
        validator={target => isNameValid}
        name="nickname"
        disabled={false}
        value={nickname}
        onChange={e => userNameOnChange(e.target.value)}
        placeholder={SIGNUP_TEXT.NICKNAME_PLACEHOLDER}
        autoComplete="off"
      />
      <Spacing size="0.25" />
      {nicknameCaption()}

      <Spacing size="2" />

      <Label htmlFor="getUserId">{SIGNUP_TEXT.ID}</Label>
      <Spacing size="0.25" />
      <Input
        type="text"
        id="getUserId"
        name="userId"
        validator={target => isIdValid}
        disabled={false}
        value={userId}
        onChange={e => userIdOnChange(e.target.value)}
        placeholder={SIGNUP_TEXT.ID_PLACEHOLDER}
        autoComplete="off"
      />
      <Spacing size="0.25" />
      {idCaption()}
      <P type="caption" color="warn">
        {isDuplicated && SIGNUP_TEXT.ID_DUPLICATE}
      </P>

      <Spacing size="2" />
      <Btn
        type="button"
        variant="key"
        disabled={btnDisable}
        onClick={e => amaddaSignUp()}
      >
        AMADDA 시작하기
      </Btn>
    </IndexLayout>
  );
}
