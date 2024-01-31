export type useFormArgs<T> = {
  initialValues: T;
  onSubmit: (data: T) => Promise<unknown>;
  validator?: (data: T) => Array<Record<keyof T, string>>;
  refInputNames?: (keyof T)[];
};
