import React, { useEffect, useState } from 'react';
import {
  Btn,
  Flex,
  Form,
  H1,
  Input,
  Label,
  P,
  Profile,
  Spacing,
  debounce,
  throttle,
} from 'external-temporal';
import { IndexLayout } from '@SH/layout/IndexLayout';
import useIdValidator from '@SH/hooks/useIdValidator';
import SIGNUP_TEXT from '@SH/constants/SIGNUP_TEXT';
import SignUpCaption from '../SignUpCaption/SignUpCaption';
import { UserInitRequest } from 'amadda-global-types';
import { http } from 'connection';
import useNameValidator from '@SH/hooks/useNameValidator';
import { useRouter } from 'next/router';

export interface SignUpProps {
  userInfo: {
    kakaoId: string;
    imageUrl: string;
    userName: string;
    isInited: boolean;
  };
}
export default function SignUp({ userInfo }: SignUpProps) {
  const router = useRouter();

  const [nameInput, setNameInput] = useState(userInfo.userName);
  const [idInput, setIdInput] = useState('');
  const [userName, setUserName] = useState(userInfo.userName);
  const [userId, setUserId] = useState('');

  const [lenCheck, setLenCheck] = useState(false);

  const {
    isIdDuplicated,
    isIdValid,
    idValue,
    setIsInitial,
    setIdValue,
    isInitial,
    isIdEmpty,
  } = useIdValidator('');
  const { nameValue, setNameValue, isNameValid } = useNameValidator(
    userInfo.userName
  );

  // const [btnDisable, setBtnDisable] = useState(true);

  // const checkAllValid = () => {
  //   console.log('체크');
  //   if (userName.length > 0 && userId.length > 3) return true;
  //   return false;
  // };

  // const btnControl = () => {
  //   console.log('버튼');
  //   if (checkAllValid()) {
  //     console.log(checkAllValid());
  //     setBtnDisable(false);
  //   } else setBtnDisable(true);
  // };

  // userName onChange
  const userNameOnChange = (value: string) => {
    const reg = /^[ㄱ-힣a-z0-9]{0,10}$/;
    if (!reg.test(value)) {
      value = value.slice(0, -1);
    }
    setUserName(value);
  };

  // userId onChagnge
  const userIdOnChange = (value: string) => {
    setIsInitial(false);
    const reg = /^[a-z0-9]{0,20}$/;
    if (!reg.test(value)) {
      value = value.slice(0, -1);
    }

    setUserId(value);
  };

  // siginup
  const amaddaSignUp = () => {
    // valid 체크
    setNameValue(userName);
    setIdValue(userId);

    if (isIdValid && !isIdDuplicated && isNameValid) {
      const UserInitRequest: UserInitRequest = {
        kakaoId: userInfo.kakaoId,
        imageUrl: userInfo.imageUrl,
        userName: userName,
        userId: userId,
      };

      http
        .post<UserInitRequest>(
          `${process.env.NEXT_PUBLIC_SHELL}/api/user/signup`,
          UserInitRequest
        )
        .then(res => {
          res.data;
        })
        .then(data => {
          router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    }
  };

  return (
    <IndexLayout>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H1>A M A D D A</H1>
        <Spacing size="2" />
        <Profile src={userInfo.imageUrl} alt="profile image" size="large" />
      </Flex>
      <Spacing size="2" />
      <Form formName="singup" onSubmit={e => amaddaSignUp()}>
        <Label htmlFor="getNickname">{SIGNUP_TEXT.NICKNAME}</Label>
        <Spacing size="0.5" />
        <Input
          type="text"
          id="getUserName"
          name="userName"
          disabled={false}
          value={userName}
          onChange={e => userNameOnChange(e.target.value)}
          placeholder={SIGNUP_TEXT.NICKNAME_PLACEHOLDER}
          autoComplete="off"
        />
        <Spacing size="0.25" />
        {userName.length === 0 ? (
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
          disabled={false}
          value={userId}
          onChange={e => userIdOnChange(e.target.value)}
          placeholder={SIGNUP_TEXT.ID_PLACEHOLDER}
          autoComplete="off"
        />
        <Spacing size="0.25" />
        {isInitial ? (
          <SignUpCaption color="grey">{SIGNUP_TEXT.ID_DESC}</SignUpCaption>
        ) : userId.length === 0 ? (
          <SignUpCaption color="warn">{SIGNUP_TEXT.ID_EMPTY}</SignUpCaption>
        ) : (
          <SignUpCaption color="grey">{SIGNUP_TEXT.ID_DESC}</SignUpCaption>
        )}
        <P type="caption" color="warn">
          {isIdDuplicated && SIGNUP_TEXT.ID_DUPLICATE}
        </P>

        <Spacing size="2" />
        <Btn type="submit" variant="key" disabled={false}>
          AMADDA 시작하기
        </Btn>
      </Form>
    </IndexLayout>
  );
}
