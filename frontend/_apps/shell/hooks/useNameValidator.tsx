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

  const [isValid, setIsValid] = useState(true);
  const [isEmpty, setIsEmpty] = useState(false);

  useEffect(() => {
    const NameCheck = async () => {
      const UserNameCheckRequestBody: NameCheckRequest = {
        userName: nickname,
      };

      // const resp = await http.post<NameCheckRequest, NameCheckResponse>(
      //   `${process.env.SPRING_API_ROOT}/user/check/name`,
      //   UserNameCheckRequestBody
      // );

      const resp = { isValid: true };

      resp.isValid ? setIsValid(true) : setIsValid(false);
    };
    NameCheck();
    nickname.length === 0 ? setIsEmpty(true) : setIsEmpty(false);
  }, [nickname]);

  // 유효성 검사

  return {
    nickname,
    setNickname,
    isNameEmpty: isEmpty,
    isNameValid: isValid,
  };
}
