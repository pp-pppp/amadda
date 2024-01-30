import { useEffect, useRef, useState } from 'react';
import type { ChangeEvent, Dispatch, FormEvent, RefObject, SetStateAction } from 'react';
import { useFormArgs } from './types';

export const useForm = <T>([key, initialValues, onSubmit, validator, refInputNames = []]: useFormArgs<T>): {
  key: string;
  values: T;
  setValues: Dispatch<SetStateAction<T>>;
  refValues: Record<keyof T, any> | null;
  handleChange: (e: ChangeEvent<HTMLInputElement & HTMLTextAreaElement>) => Promise<void>;
  invalids: Array<Record<keyof T, string>>;
  refs: Record<(typeof refInputNames)[number], RefObject<HTMLInputElement>> | null;
  submit: (data: any) => Promise<unknown>;
  isLoading: boolean;
  result: unknown;
} => {
  const [values, setValues] = useState<typeof initialValues>({ ...initialValues } as const);
  const [invalids, setInvalids] = useState<Array<Record<keyof T, string>>>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [result, setResult] = useState<unknown>(null);

  const handleChange = async (e: ChangeEvent<HTMLInputElement & HTMLTextAreaElement>) => {
    const { name, value, checked, type } = e.target;
    if (type === 'checkbox') {
      setValues({ ...values, [name]: checked });
    } else setValues({ ...values, [name]: value });
  };

  const refInputNamesType = [...refInputNames] as const;
  const [currRefValues, refs] = useRefInputInit<T>(refInputNamesType);

  const convertedRefValues = typeof currRefValues === 'function' ? currRefValues() : currRefValues;

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    validator && setInvalids(validator({ ...values, ...convertedRefValues }));
    return result;
  };

  useEffect(() => {
    isLoading &&
      (async () => {
        if (!invalids || Object.keys(invalids).length === 0) {
          if (refInputNames) setValues({ ...values, ...convertedRefValues });
          else setValues({ ...values });

          const response = await onSubmit(values);
          setResult(response);
          setIsLoading(false);
        }
      })();
  }, [invalids]);

  const data = {
    key,
    values,
    setValues,
    refValues: convertedRefValues,
    handleChange,
    invalids,
    refs,
    submit,
    isLoading,
    result,
  };

  return data;
};

function useRefInputInit<T>(
  refInputNames: readonly (keyof T)[] = []
): [() => Record<(typeof refInputNames)[number], any>, Record<(typeof refInputNames)[number], RefObject<HTMLInputElement>>] {
  type Refs = Record<(typeof refInputNames)[number], RefObject<HTMLInputElement>>;
  type RefValues = Record<(typeof refInputNames)[number], any>;

  const refs: Refs = {} as Refs;
  refInputNames.forEach(k => (refs[k] = useRef<HTMLInputElement>(null)));

  const currRefValues: () => RefValues = () => {
    const refValues: RefValues = {} as RefValues;
    refInputNames.forEach(k => (refValues[k] = refs[k]?.current?.value || ''));
    return refValues;
  };

  return [currRefValues, refs];
}
