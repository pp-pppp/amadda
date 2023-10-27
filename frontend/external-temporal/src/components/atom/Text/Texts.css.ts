import colors from '@/constants/colors';
import { styleVariants } from '@vanilla-extract/css';

export const COLOR = styleVariants({
  key: { color: colors.key },
  black: { color: colors.black },
  white: { color: colors.white },
  warn: { color: colors.category.salmon },
  ok: { color: colors.category.green },
});
