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
  const [isIdEmpty, setIsIdEmpty] = useState(false);
  const [isIdValid, setIsIdValid] = useState(true);
  const [isIdDuplicated, setIsIdDuplicated] = useState(false);
  const [idValue, setIdValue] = useState('');
  const [initial, setInitial] = useState(true);

  useEffect(() => {
    const IdCheck = async () => {
      const UserIdCheckRequestBody: IdCheckRequest = {
        userId: idValue,
      };

      // const {isDuplicated, isValid} = await http.post<IdCheckRequest, IdCheckResponse>(
      //   `${process.env.SPRING_API_ROOT}/user/check/id`,
      //   UserIdCheckRequestBody
      // );
      // isDuplicated ? setIsIdDuplicated(true) : setIsIdDuplicated(false);
      // !isValid && setIdValue(idValue.slice(0, -1));

      const resp = { isDuplicated: false, isValid: true };

      resp.isDuplicated ? setIsIdDuplicated(true) : setIsIdDuplicated(false);
      // !resp.isValid && setIdValue(idValue.slice(0, -1));
    };

    IdCheck();

    initial && setInitial(false);
    !initial && idValue.length === 0 ? setIsIdEmpty(true) : setIsIdEmpty(false);
    idValue.length > 20 && setIdValue(idValue.slice(0, -1));

    setUserId(idValue);
  }, [idValue]);

  return {
    userId,
    idValue,
    setIdValue,
    isIdDuplicated: isIdDuplicated,
    isIdValid: isIdValid,
    isIdEmpty: isIdEmpty,
  };
}
