import colors from '#/constants/colors';
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
