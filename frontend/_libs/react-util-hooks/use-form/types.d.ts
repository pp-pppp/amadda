export type useFormArgs<T> = [
  key: string,
  initialValues: T,
  onSubmit: (data: T) => Promise<unknown>,
  validator?: (data: T) => Array<Record<keyof T, string>>,
  refInputNames: (keyof T)[],
];
