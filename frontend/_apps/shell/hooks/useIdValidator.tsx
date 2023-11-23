import { http } from '@SH/utils/http';
import { UserIdCheckRequest, UserIdCheckResponse } from 'amadda-global-types';
import { useState, useEffect } from 'react';

export default function useIdValidator(id: string) {
  const [userId, setUserId] = useState<string>(id);
  const [idValue, setIdValue] = useState(id);

  const [isIdDuplicated, setIsIdDuplicated] = useState(false);
  const [isIdValid, setIsIdValid] = useState(true);
  const [isIdEmpty, setIsIdEmpty] = useState(false);

  const [isInitial, setIsInitial] = useState(true);

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
      .then(json => json.data)
      .then(data => {
        data.isDuplicated ? setIsIdDuplicated(true) : setIsIdDuplicated(false);

        if (data.isValid) {
          setIsIdValid(data.isValid);
          setUserId(idValue);
          setIsIdEmpty(false);
          return;
        }

        if (isInitial) {
          setIsIdValid(true);
          return;
        }

        if (!isInitial && idValue.length < 3) {
          setIsIdValid(true);
          setIsIdEmpty(true);
          setUserId(idValue);
        } else {
          setIsIdValid(false);
        }
      });
  }, [idValue]);

  return {
    userId,
    idValue,
    setIdValue,
    isInitial,
    setIsInitial,
    isIdDuplicated: isIdDuplicated,
    isIdValid: isIdValid,
    isIdEmpty: isIdEmpty,
    setIsIdEmpty,
  };
}
