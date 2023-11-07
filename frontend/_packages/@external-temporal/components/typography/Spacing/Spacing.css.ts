import { styleVariants } from '@vanilla-extract/css';

export const VSIZE = styleVariants({
  '0.25': {
    height: '0.25rem',
  },
  '0.5': {
    height: '0.5rem',
  },
  '1': {
    height: '1rem',
  },
  '2': {
    height: '2rem',
  },
  '3': {
    height: '3rem',
  },
  '5': {
    height: '5rem',
  },
  '10': {
    height: '10rem',
  },
});
export const HSIZE = styleVariants({
  '0.25': {
    width: '0.25rem',
  },
  '0.5': {
    width: '0.5rem',
  },
  '1': {
    width: '1rem',
  },
  '2': {
    width: '2rem',
  },
  '3': {
    width: '3rem',
  },
  '5': {
    width: '5rem',
  },
  '10': {
    width: '10rem',
  },
});
