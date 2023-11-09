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
  const [idValue, setIdValue] = useState('');

  useEffect(() => {
    const IdCheck = async () => {
      const UserIdCheckRequestBody: IdCheckRequest = {
        userId: idValue,
      };

      // const resp = await http.post<IdCheckRequest, IdCheckResponse>(
      //   `${process.env.SPRING_API_ROOT}/user/check/id`,
      //   UserIdCheckRequestBody
      // );

      const resp = { isDuplicated: false, isValid: true };

      resp.isDuplicated ? setIsDuplicated(true) : setIsDuplicated(false);
      // !resp.isValid && setIdValue(idValue.slice(0, -1));
    };

    IdCheck();

    idValue.length === 0 ? setIsEmpty(true) : setIsEmpty(false);
    idValue.length > 20 && setIdValue(idValue.slice(0, -1));

    setUserId(idValue);
  }, [idValue]);

  useEffect(() => {
    setIsEmpty(false);
  }, []);

  return {
    userId,
    idValue,
    setIdValue,
    isDuplicated: isDuplicated,
    isIdValid: isValid,
    isIdEmpty: isEmpty,
  };
}
