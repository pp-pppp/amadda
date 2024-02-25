import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import type { ChangeEvent, FormEvent, RefObject } from 'react';
import type { UseForm, UseFormArgs } from './types';

export const useForm = <T>({
  initialValues,
  onSubmit,
  validator,
  refInputNames = [],
  setExternalStoreValues = undefined,
  setExternalStoreData = undefined,
}: UseFormArgs<T>): UseForm<T> | void => {
  const [values, setValues] = useState<typeof initialValues>({ ...initialValues } as const);
  const [invalidFields, setInvalidFields] = useState<Array<Record<keyof T, string>>>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<unknown>(null);

  const saveValue = useCallback(
    (data: T) => {
      if (typeof setExternalStoreValues !== 'function' && typeof setExternalStoreData !== 'undefined')
        console.error('<!> useForm: setExternalStoreValues should be a function');
      typeof setExternalStoreValues === 'function' ? setExternalStoreValues(data) : setValues(data);
    },
    [setExternalStoreValues]
  );

  const handleChange = useCallback(
    async (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      const { name, value } = e.target;
      saveValue({ ...values, [name]: value });
    },
    [saveValue, setValues]
  );

  const refInputNamesType = [...refInputNames] as const;
  const [currRefValues, refs] = useRefInputInit<T>(refInputNamesType);
  const convertedRefValues = typeof currRefValues === 'function' ? currRefValues() : currRefValues;

  const submit = useCallback(
    async (e: FormEvent) => {
      e.preventDefault();
      setIsLoading(true);
      validator && setInvalidFields(validator({ ...values, ...convertedRefValues }));
    },
    [setIsLoading, validator, setInvalidFields, values, convertedRefValues]
  );

  useEffect(() => {
    isLoading &&
      (async () => {
        if (!invalidFields || Object.keys(invalidFields).length === 0) {
          if (!refInputNames) setValues({ ...values });
          else saveValue({ ...values, ...convertedRefValues });

          const res = await onSubmit(values);
          setResponse(res);
          setIsLoading(false);
        }
      })();
  }, [isLoading, invalidFields]);

  const data = useMemo(
    () => ({
      values,
      setValues,
      refValues: convertedRefValues,
      handleChange,
      invalidFields,
      refs,
      submit,
      isLoading,
      response,
    }),
    [values, setValues, convertedRefValues, handleChange, invalidFields, refs, submit, isLoading, response]
  );

  const updateExternalStore = useCallback(() => {
    if (typeof setExternalStoreData !== 'function' && typeof setExternalStoreData !== 'undefined')
      console.error('<!> useForm: setExternalStoreData should be a function');
    typeof setExternalStoreData === 'function' && setExternalStoreData(data);
  }, [data, setExternalStoreData]);

  useEffect(() => {
    updateExternalStore();
  }, [setExternalStoreData]);

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
