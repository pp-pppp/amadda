import { UserNameCheckRequest, UserNameCheckResponse } from '@amadda/global-types';
import { useState, useEffect } from 'react';

export default function useNameValidator(name: string) {
  const [userName, setuserName] = useState<string>(name);
  const [nameValue, setNameValue] = useState(name);

  const [isNameValid, setIsNameValid] = useState(true);
  const [isNameEmpty, setIsNameEmpty] = useState(false);

  useEffect(() => {
    const UserNameCheckRequestBody: UserNameCheckRequest = {
      userName: nameValue,
    };

    fetch(`${process.env.NEXT_PUBLIC_SHELL}/api/user/check/name`, {
      method: 'POST',
      body: JSON.stringify(UserNameCheckRequestBody),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json => json.data)
      .then(data => {
        if (data.isValid) {
          setIsNameValid(data.isValid);
          setuserName(nameValue);
          return;
        }

        if (nameValue.length === 0) {
          setIsNameValid(true);
          setIsNameEmpty(true);
        } else {
          setIsNameValid(false);
        }
      });
  }, [nameValue]);

  return {
    userName,
    setuserName,
    nameValue,
    setNameValue,
    isNameEmpty: isNameEmpty,
    isNameValid: isNameValid,
  };
}
