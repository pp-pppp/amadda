// // import { http } from '@SH/utils/http';
// import {
//   UserNameCheckRequest,
//   UserNameCheckResponse,
// } from 'amadda-global-types';
// import { http, clientHttp } from 'connection';
// import { useState, useEffect } from 'react';

// export interface NameCheckRequest {
//   userName: string;
// }

// export interface NameCheckResponse {
//   isValid: boolean;
// }

// export default function useNameValidator(name: string) {
//   const [nickname, setNickname] = useState<string>(name);
//   const [isNameValid, setIsNameValid] = useState(true);
//   const [isNameEmpty, setIsNameEmpty] = useState(false);
//   const [nameValue, setNameValue] = useState(name);

//   useEffect(() => {
//     // const NameCheck = () => {
//     const UserNameCheckRequestBody: UserNameCheckRequest = {
//       userName: nameValue,
//     };

//     fetch(`${process.env.NEXT_PUBLIC_SHELL}/api/user/check/name`, {
//       method: 'POST',
//       body: JSON.stringify(UserNameCheckRequestBody),
//       headers: {
//         'Content-Type': 'application/json',
//       },
//     })
//       .then(res => res.json())
//       .then(json => {
//         setIsNameValid(json.data.isValid);
//         return isNameValid;
//       })
//       .then(isValid => {});
//     nameValue.length === 0 ? setIsNameEmpty(true) : setIsNameEmpty(false);
//     nameValue.length > 20 && setNameValue(nameValue.slice(0, -1));
//     !isNameValid && setNameValue(nameValue.slice(0, -1));
//     setNickname(nameValue);
//   }, [nameValue]);

//   return {
//     nickname,
//     setNickname,
//     nameValue,
//     setNameValue,
//     isNameEmpty: isNameEmpty,
//     isNameValid: isNameValid,
//   };
// }
