import { useState, useEffect } from 'react';
import { http } from '#/util/http';

export interface IdCheckRequest {
  userId: string;
}

export interface IdCheckResponse {
  isDuplicated: boolean;
  isValid: boolean;
}

export default function useIdValidator(id: string) {
  const [userId, setUserId] = useState(id);
  const [isEmpty, setIsEmpty] = useState(false);
  const [isValid, setIsValid] = useState(true);
  const [isDuplicated, setIsDuplicated] = useState(false);

  useEffect(() => {
    const IdCheck = async () => {
      const UserIdCheckRequestBody: IdCheckRequest = {
        userId: userId,
      };

      // const resp = await http.post<IdCheckRequest, IdCheckResponse>(
      //   `${process.env.SPRING_API_ROOT}/user/check/id`,
      //   UserIdCheckRequestBody
      // );

      const resp = { isDuplicated: false, isValid: true };

      resp.isDuplicated ? setIsDuplicated(true) : setIsDuplicated(false);
      resp.isValid ? setIsValid(true) : setIsValid(false);
    };
    IdCheck();
    userId.length === 0 ? setIsEmpty(true) : setIsEmpty(false);
  }, [userId]);

  useEffect(() => {
    setIsEmpty(false);
  }, []);

  // ID 중복 및 유효성 검사

  return {
    userId,
    setUserId,
    isDuplicated: isDuplicated,
    isIdValid: isValid,
    isIdEmpty: isEmpty,
  };
}
