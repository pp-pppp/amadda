let timer: number;

export const throttle = (fn: () => void, delay: number) => {
  if (typeof window === 'undefined') return;
  if (timer) return;

  timer = window.setTimeout(() => {
    fn;
  }, delay);

  return window.clearTimeout(timer);
};
