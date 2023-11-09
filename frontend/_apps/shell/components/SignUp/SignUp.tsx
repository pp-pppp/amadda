import React, { useEffect, useState } from 'react';
import { Btn, Flex, Form, H1, Input, Label, P, Spacing } from '#/index';
import { IndexLayout } from '@/layout/IndexLayout';
import { useSession } from 'next-auth/react';
import { Profile } from '../../../user/components/Profile/Profile';
import useNameValidator from '@/hooks/useNameValidator';
import useIdValidator from '@/hooks/useIdValidator';
import SIGNUP_TEXT from '@/constants/SIGNUP_TEXT';
import SignUpCaption from '../SignUpCaption/SignUpCaption';

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

  const {
    nickname,
    setNickname,
    isNameEmpty,
    isNameValid,
    nameValue,
    setNameValue,
  } = useNameValidator(userName);
  const { userId, isIdEmpty, isDuplicated, isIdValid, setIdValue } =
    useIdValidator('');

  const [btnDisable, setBtnDisable] = useState(true);

  useEffect(() => {
    // setNickname(userName);
    setNameValue(userName);
  }, [session]);

  const checkAllValid = () => {
    if (
      !isDuplicated &&
      !isNameEmpty &&
      !isIdEmpty &&
      isIdValid &&
      isNameValid &&
      userId.length !== 0
    )
      return true;
    return false;
  };

  const btnControl = () => {
    if (checkAllValid()) setBtnDisable(false);
    else setBtnDisable(true);
  };

  // userName onChange
  const userNameOnChange = (value: string) => {
    setNameValue(value);
    btnControl();
  };

  // userId onChagnge
  const userIdOnChange = (value: string) => {
    setIdValue(value);
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

  return (
    <IndexLayout>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H1>A M A D D A</H1>
        <Spacing size="2" />
        <Profile src={imageUrl} alt="profile image" size="large" />
      </Flex>
      <Spacing size="2" />
      <Form formName="singup" onSubmit={e => amaddaSignUp()}>
        <Label htmlFor="getNickname">{SIGNUP_TEXT.NICKNAME}</Label>
        <Spacing size="0.25" />
        <Spacing size="0.25" />
        <Input
          type="text"
          id="getNickname"
          validator={target => isNameValid}
          name="nickname"
          disabled={false}
          value={nameValue}
          onChange={e => userNameOnChange(e.target.value)}
          placeholder={SIGNUP_TEXT.NICKNAME_PLACEHOLDER}
          autoComplete="off"
        />
        <Spacing size="0.25" />
        {isNameEmpty ? (
          <SignUpCaption color="warn">
            {SIGNUP_TEXT.NICKNAME_EMPTY}
          </SignUpCaption>
        ) : (
          <SignUpCaption color="grey">
            {SIGNUP_TEXT.NICKNAME_DESC}
          </SignUpCaption>
        )}
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
        {isIdEmpty ? (
          <SignUpCaption color="warn">{SIGNUP_TEXT.ID_EMPTY}</SignUpCaption>
        ) : (
          <SignUpCaption color="grey">{SIGNUP_TEXT.ID_DESC}</SignUpCaption>
        )}
        <P type="caption" color="warn">
          {isDuplicated && SIGNUP_TEXT.ID_DUPLICATE}
        </P>

        <Spacing size="2" />
        <Btn type="submit" variant="key" disabled={btnDisable}>
          AMADDA 시작하기
        </Btn>
      </Form>
    </IndexLayout>
  );
}
