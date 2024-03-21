import { useCallback, useEffect } from 'react';

export const useDebouncedEffect = (fn: Function | (() => Function), delay: number, deps: any[]) => {
  const callback = useCallback(fn, deps);

  useEffect(() => {
    const t = setTimeout(() => {
      callback();
    }, delay);

    return () => {
      clearTimeout(t);
    };
  }, [fn, delay]);
};
