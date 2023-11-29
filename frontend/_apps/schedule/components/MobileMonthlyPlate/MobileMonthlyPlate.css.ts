import { style, styleVariants } from '@vanilla-extract/css';
import colors from '#/constants/colors';

export const BASE = style({
  // width: 'calc(430px / 7)',
  // maxWidth: 'calc(430px / 7)',
  textAlign: 'center',
  height: '3.5rem',
  border: `1px solid transparent`,

  display: 'grid',
  gridTemplateRows: '1fr 1fr',
  alignItems: 'flex-end',

  cursor: 'pointer',
});

export const DATE = style({
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  borderRadius: 50,
  // padding: '1rem',
  width: '2rem',
  height: '2rem',
  textAlign: 'center',
});

export const SELECTED = style({
  backgroundColor: colors.key,
});

export const SPAN = styleVariants({
  WEEKDAY: {
    textAlign: 'center',
    color: colors.BLACK,
  },
  WEEKEND: {
    textAlign: 'center',
    color: colors.category.salmon,
  },
  OTHER: {
    textAlign: 'center',
    color: colors.GREY_400,
  },
});

export const SELECTED_SPAN = style({
  textAlign: 'center',
  color: colors.white,
});
