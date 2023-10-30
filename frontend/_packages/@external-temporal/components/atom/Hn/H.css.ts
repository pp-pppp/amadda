import { style, styleVariants } from '@vanilla-extract/css';

export const BOLD = style({
  fontFamily: 'bold',
});
export const VARIANTS = styleVariants({
  '1': [
    BOLD,
    {
      fontSize: '2.75rem',
    },
  ],
  '2': [
    BOLD,
    {
      fontSize: '2rem',
    },
  ],
  '3': {
    fontSize: '1.5rem',
  },
  '4': {
    fontSize: '1.25rem',
  },
  '5': [
    BOLD,
    {
      fontSize: '1rem',
    },
  ],
  '6': {
    fontSize: '1rem',
  },
});
