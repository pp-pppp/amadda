import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  backgroundColor: colors.key,
  borderRadius: '3rem',
  border: 'none',
  height: '3rem',
  width: '3rem',
  ':hover': {
    backgroundColor: colors.keyHover,
  },
});
