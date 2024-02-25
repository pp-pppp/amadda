let timer: number;
export const debounce = (fn: Function | (() => Function), delay: number) => {
  if (typeof window === 'undefined') return;
  clearTimeout(timer);
  timer = window.setTimeout(() => {
    fn();
  }, delay);
};
