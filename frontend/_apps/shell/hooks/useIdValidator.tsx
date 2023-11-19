import { http } from '@SH/utils/http';
import { UserIdCheckRequest, UserIdCheckResponse } from 'amadda-global-types';
import { useState, useEffect } from 'react';

export default function useIdValidator(id: string) {
  const [userId, setUserId] = useState<string>(id);
  const [isIdEmpty, setIsIdEmpty] = useState(false);
  const [isIdValid, setIsIdValid] = useState(true);
  const [isIdDuplicated, setIsIdDuplicated] = useState(false);
  const [idValue, setIdValue] = useState(id);
  const [initial, setInitial] = useState(true);
  useEffect(() => {
    const UserIdCheckRequestBody: UserIdCheckRequest = {
      userId: idValue,
    };

    fetch(`${process.env.NEXT_PUBLIC_SHELL}/api/user/check/id`, {
      method: 'POST',
      body: JSON.stringify(UserIdCheckRequestBody),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json => {
        json.data.isDuplicated
          ? setIsIdDuplicated(true)
          : setIsIdDuplicated(false);
        initial && setInitial(false);
        !initial && idValue.length === 0
          ? setIsIdEmpty(true)
          : setIsIdEmpty(false);
        idValue.length > 20 && setIdValue(idValue.slice(0, -1));
      });

    setUserId(idValue);
  }, [idValue]);

  return {
    userId,
    idValue,
    setIdValue,
    isIdDuplicated: isIdDuplicated,
    isIdValid: isIdValid,
    isIdEmpty: isIdEmpty,
    setIsIdEmpty,
  };
}
