import { colors } from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const BOLD = style({
  fontFamily: 'bold',
  fontWeight: '700',
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

export const COLOR = styleVariants({
  key: { color: colors.key },
  black: { color: colors.black },
  grey: { color: colors.GREY_600 },
  lightgrey: { color: colors.GREY_400 },
  white: { color: colors.white },
  warn: { color: colors.category.salmon },
  ok: { color: colors.category.green },
});
