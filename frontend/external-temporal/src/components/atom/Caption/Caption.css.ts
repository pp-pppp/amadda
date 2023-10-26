import colors from '@/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

const BASE = style({
  fontSize: '0.9rem',
});

export const TYPE = styleVariants({
  none: [
    BASE,
    {
      display: 'hidden',
    },
  ],
  desc: [
    BASE,
    {
      color: colors.GREY_800,
    },
  ],
  warn: [
    BASE,
    {
      color: colors.category.salmon,
    },
  ],
});
