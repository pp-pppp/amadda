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
} from 'external-temporal';
import { IndexLayout } from '@SH/layout/IndexLayout';
import { useSession } from 'next-auth/react';
import useIdValidator from '@SH/hooks/useIdValidator';
import SIGNUP_TEXT from '@SH/constants/SIGNUP_TEXT';
import SignUpCaption from '../SignUpCaption/SignUpCaption';
import { UserInitRequest } from 'amadda-global-types';
import { http } from '@SH/utils/http';

export default function SignUp() {
  const { data: session, update } = useSession();
  const [kakaoId, setKakaoId] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [userName, setUserName] = useState('');
  // const userSeq = session?.user.userId || '';
  // const imageUrl = session?.user.imageUrl || '';
  // const userName = session?.user.userName || '';

  const [nameValue, setNameValue] = useState<string>('');
  const [isIdEmpty, setIsIdEmpty] = useState(false);

  const { userId, idValue, isIdDuplicated, isIdValid, setIdValue } =
    useIdValidator('');

  const [btnDisable, setBtnDisable] = useState(true);

  useEffect(() => {
    if (session) {
      setKakaoId(session.user.kakaoId);
      setImageUrl(session.user.imageUrl);
      setUserName(session.user.userName);
    }
  }, [session]);

  useEffect(() => {
    userName && setNameValue(userName);
  }, [userName]);

  const checkAllValid = () => {
    if (!isIdDuplicated && !isIdEmpty && isIdValid && idValue.length !== 0)
      return true;
    return false;
  };
  useEffect(() => {
    if (checkAllValid()) setBtnDisable(false);
    else setBtnDisable(true);
  }, [isIdDuplicated, isIdEmpty, isIdValid]);

  const btnControl = () => {
    if (checkAllValid()) setBtnDisable(false);
    else setBtnDisable(true);
  };

  // userName onChange
  const userNameOnChange = (value: string) => {
    const reg = /^[^A-Z!@#$%^&*`\-()_+={}|\[\]\\:";'<>?,./\s]{0,20}$/;
    if (!reg.test(value)) {
      value = value.slice(0, -1);
    }
    setNameValue(value);
    btnControl();
  };

  // userId onChagnge
  const userIdOnChange = (value: string) => {
    const reg =
      /^[^A-Z!@#$%^&*`\-()_+={}|\[\]\\:";'<>?,.\s\u3131-\uD79D]{0,20}$/;
    if (!reg.test(value)) {
      value = value.slice(0, -1);
    }
    value.length === 0 ? setIsIdEmpty(true) : setIsIdEmpty(false);
    setIdValue(value);

    btnControl();
    console.log(isIdDuplicated);
  };

  // siginup
  const amaddaSignUp = async () => {
    if (checkAllValid()) {
      await update({ userName: nameValue, userId: userId, isInited: true });

      console.log(userName);
      const UserInitRequest: UserInitRequest = {
        kakaoId: kakaoId,
        imageUrl: imageUrl,
        userName: userName,
        userId: userId,
      };

      console.log(UserInitRequest);

      http
        .post<UserInitRequest>(
          `${process.env.NEXT_PUBLIC_SHELL}/api/user/signup`,
          UserInitRequest
        )
        .then(res => {
          res.data;
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
          // validator={target => }
          name="nickname"
          disabled={false}
          value={nameValue}
          onChange={e => userNameOnChange(e.target.value)}
          placeholder={SIGNUP_TEXT.NICKNAME_PLACEHOLDER}
          autoComplete="off"
        />
        <Spacing size="0.25" />
        {nameValue.length === 0 ? (
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
          {isIdDuplicated && SIGNUP_TEXT.ID_DUPLICATE}
        </P>

        <Spacing size="2" />
        <Btn type="submit" variant="key" disabled={btnDisable}>
          AMADDA 시작하기
        </Btn>
      </Form>
    </IndexLayout>
  );
}
