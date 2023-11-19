import { http } from '@SH/utils/http';
import { UserIdCheckRequest, UserIdCheckResponse } from 'amadda-global-types';
import { useState, useEffect } from 'react';

export default function useIdValidator(id: string) {
  const [userId, setUserId] = useState(id);
  const [isIdEmpty, setIsIdEmpty] = useState(false);
  const [isIdValid, setIsIdValid] = useState(true);
  const [isIdDuplicated, setIsIdDuplicated] = useState(false);
  const [idValue, setIdValue] = useState('');
  const [initial, setInitial] = useState(true);

  useEffect(() => {
    const IdCheck = async () => {
      const UserIdCheckRequestBody: UserIdCheckRequest = {
        userId: idValue,
      };

      await http
        .post<UserIdCheckRequest, UserIdCheckResponse>(
          `${process.env.NEXT_PUBLIC_SHELL}/api/user/check/id`,
          UserIdCheckRequestBody
        )
        .then(res => res.data)
        .then(data => {
          data.isDuplicated
            ? setIsIdDuplicated(true)
            : setIsIdDuplicated(false);
          !data.isValid && setIdValue(idValue.slice(0, -1));
        });
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
