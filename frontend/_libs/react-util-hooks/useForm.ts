import { useEffect, useRef, useState } from 'react';
import type { ChangeEvent, FormEvent, RefObject } from 'react';

/**
 * @description form 컴포넌트와 함께 사용합니다.
 *
 * @param initialValue 각 input의 초기값을 지정합니다.
 * 이 값은 페이지가 로딩되었을 때 기본적으로 들어가 있게 됩니다.
 * @param onSubmit submit 메서드를 콜백으로 보냅니다.
 * submit 메서드에는 input에 입력한 값들이 <T>로 묶여 string type으로 들어옵니다.
 * @param validator 각 input 값을 validate하는 함수를 인자로 받습니다.
 * 이상이 있는 input name을 key로 하는 오류 메시지와 함께 리턴해주세요.
 * @param refKeys <T> 중 ref Input을 사용할 input name들을 배열로 전달합니다.
 * @returns `{ values, refValues, invalids, handleChange, refs, submit, isLoading, result }`
 */
export default function useForm<T>(initialValue: T, onSubmit: (arg: T) => unknown, validator?: (arg: T) => unknown, refKeys?: Array<keyof Partial<T>>) {
  const [values, setValues] = useState<T>(initialValue);
  const [invalids, setInvalids] = useState<unknown>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [result, setResult] = useState<unknown>(null);

  const handleChange = async (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setValues({ ...values, [name]: value });
  };

  const [currRefValues, refs] = useRefInput<T>(refKeys);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    validator && setInvalids(validator({ ...values, ...refs }));
    return result;
  };

  useEffect(() => {
    isLoading &&
      (async () => {
        if (!invalids || Object.keys(invalids).length === 0) {
          if (refKeys) setValues({ ...values, ...currRefValues });
          else setValues({ ...values });

          const response = await onSubmit(values);
          setResult(response);
        }
        setIsLoading(false);
      })();
  }, [invalids]);

  return {
    values,
    refValues: currRefValues,
    invalids,
    handleChange,
    refs,
    submit,
    isLoading,
    result,
  };
}

/**
 *
 * @param refKeys 비제어 input을 사용할 input name 배열을 인자로 받습니다.
 * @returns [currRefValues, refs] 현재 비제어 인풋들의 k:v 쌍을 모은 배열과, 해당 input들의 MutableRefObject 객체(초기값 null)을 리턴합니다.
 */
function useRefInput<T>(refKeys: Array<keyof Partial<T>> | undefined) {
  const refs: Record<keyof Partial<T>, RefObject<HTMLInputElement>> = {} as Record<keyof Partial<T>, RefObject<HTMLInputElement>>;

  if (!refKeys) return [refs, null];

  refKeys.forEach(k => {
    refs[k] = useRef<HTMLInputElement>(null);
  });

  const currRefValues = () => {
    const refValues: Record<string, any>[] = [];
    if (Object.keys(refs).length > 0)
      for (const key in refs) {
        refValues.push({ [key]: refs[key].current?.value || '' });
      }
    return refValues;
  };

  return [currRefValues, refs];
}
