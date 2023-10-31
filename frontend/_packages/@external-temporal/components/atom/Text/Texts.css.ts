import colors from '#/constants/colors';
import { styleVariants } from '@vanilla-extract/css';

export const COLOR = styleVariants({
  key: { color: colors.key },
  black: { color: colors.black },
  grey: { color: colors.GREY_600 },
  lightgrey: { color: colors.GREY_400 },
  white: { color: colors.white },
  warn: { color: colors.category.salmon },
  ok: { color: colors.category.green },
  blue: { color: colors.category.blue },
});
