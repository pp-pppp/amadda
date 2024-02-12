import { colors } from '#/style/colors';
import { styleVariants } from '@vanilla-extract/css';

export const COLOR = styleVariants({
  key: { fill: colors.key },
  black: { fill: colors.black },
  white: { fill: colors.white },
  lightgrey: { fill: colors.GREY_300 },
  darkgrey: { fill: colors.GREY_600 },
});
export const CURSOR = styleVariants({
  default: { cursor: 'default' },
  pointer: { cursor: 'pointer' },
});
export const SIZE = styleVariants({
  M: { width: '1.5rem', height: '1.5rem' },
  L: { width: '3rem', height: '3rem' },
});
