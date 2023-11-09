import { useState, useEffect } from 'react';

import { http } from '#/util/http';

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
      const UserNameCheckRequestBody: NameCheckRequest = {
        userName: nameValue,
      };

      // const resp = await http.post<NameCheckRequest, NameCheckResponse>(
      //   `${process.env.SPRING_API_ROOT}/user/check/name`,
      //   UserNameCheckRequestBody
      // );

      const resp = { isValid: true };
      !resp.isValid && setNameValue(nickname.slice(0, -1));
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
