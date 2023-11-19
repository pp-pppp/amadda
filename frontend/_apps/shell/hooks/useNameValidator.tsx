import { http } from '@SH/utils/http';
import {
  UserNameCheckRequest,
  UserNameCheckResponse,
} from 'amadda-global-types';
import { useState, useEffect } from 'react';

export interface NameCheckRequest {
  userName: string;
}

export interface NameCheckResponse {
  isValid: boolean;
}

export default function useNameValidator(name: string) {
  const [nickname, setNickname] = useState<string>(name);
  const [isNameValid, setIsNameValid] = useState(true);
  const [isNameEmpty, setIsNameEmpty] = useState(false);
  const [nameValue, setNameValue] = useState(name);

  useEffect(() => {
    const NameCheck = async () => {
      const UserNameCheckRequestBody: UserNameCheckRequest = {
        userName: nameValue,
      };

      await http
        .post<UserNameCheckRequest, UserNameCheckResponse>(
          `${process.env.NEXT_PUBLIC_SHELL}/api/user/check/id`,
          UserNameCheckRequestBody
        )
        .then(res => res.data)
        .then(data => {
          !data.isValid && setNameValue(nickname.slice(0, -1));
        });
    };

    NameCheck();
    nameValue.length === 0 ? setIsNameEmpty(true) : setIsNameEmpty(false);
    nameValue.length > 20 && setNameValue(nameValue.slice(0, -1));

    setNickname(nameValue);
  }, [nameValue]);

  return {
    nickname,
    setNickname,
    nameValue,
    setNameValue,
    isNameEmpty: isNameEmpty,
    isNameValid: isNameValid,
  };
}
