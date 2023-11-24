let timer: number;
export const debounce = (fn: () => void, delay: number) => {
  if (typeof window === 'undefined') return;
  clearTimeout(timer);
  timer = window.setTimeout(() => {
    fn();
  }, delay);
};
