import { useEffect, useRef, useState } from 'react';
import type { ChangeEvent, FormEvent, RefObject } from 'react';
import type { UseForm, UseFormArgs } from './types';

export const useForm = <T>({
  initialValues,
  onSubmit,
  validator,
  refInputNames = [],
  setExternalStoreValues = () => {},
  setExternalStoreData = () => {},
}: UseFormArgs<T>): UseForm<T> | void => {
  const [values, setValues] = useState<typeof initialValues>({ ...initialValues } as const);
  const [invalids, setInvalids] = useState<Array<Record<keyof T, string>>>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [result, setResult] = useState<unknown>(null);

  const saveExternalStoreValue = (value: T) => {
    typeof setExternalStoreValues === 'function' ? setExternalStoreValues(value) : console.error('<!> useForm: setExternalStoreValues should be a function');
  };

  const handleChange = async (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    saveExternalStoreValue({ ...values, [name]: value });
    setValues({ ...values, [name]: value });
  };

  const refInputNamesType = [...refInputNames] as const;
  const [currRefValues, refs] = useRefInputInit<T>(refInputNamesType);

  const convertedRefValues = typeof currRefValues === 'function' ? currRefValues() : currRefValues;

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
          if (!refInputNames) setValues({ ...values });
          else {
            saveExternalStoreValue({ ...values, ...currRefValues });
            setValues({ ...values, ...currRefValues });
          }

          const response = await onSubmit(values);
          setResult(response);
          setIsLoading(false);
        }
      })();
  }, [invalids]);

  const data = {
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

  typeof setExternalStoreData === 'function'
    ? useEffect(() => {
        const updateExternalStore = () => {
          setExternalStoreData(data);
        };
        updateExternalStore();
      }, [setExternalStoreData])
    : console.error('<!> useForm: setExternalStoreData should be a function');

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
