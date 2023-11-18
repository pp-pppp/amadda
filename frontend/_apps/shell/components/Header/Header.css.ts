import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  height: '2rem',
  paddingBottom: '0.5rem',
});
export const MENU = style({
  height: '2rem',
  background: 'transparent',
  border: 'none',
  borderRadius: '0.5rem',
  ':hover': {
    background: colors.GREY_100,
  },
  ':active': {
    background: colors.GREY_100,
  },
});
