import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  width: 'calc(100vw - 2rem)',
  maxWidth: '430px',
  height: '2.5rem',
  paddingBottom: '0.5rem',
});
export const ICON = style({});
export const MENU = style({
  width: '3.25rem',
  height: '2.5rem',
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
