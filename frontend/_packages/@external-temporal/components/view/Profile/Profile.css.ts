import { styleVariants } from '@vanilla-extract/css';

export const FRAME = styleVariants({
  large: {
    position: 'relative',
    width: '5rem',
    height: '5rem',
  },
  medium: {
    position: 'relative',
    width: '3rem',
    height: '3rem',
  },
});
