import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

const BASE = style({
  backgroundColor: colors.GREY_200,
  border: 'none',
  borderRadius: '0.5rem',
  color: colors.black,
  padding: '1rem',
  width: '100%',
  lineHeight: '1.35',

  ':focus': {
    outline: `0.1rem solid ${colors.key}`,
  },

  '::-webkit-scrollbar': {
    width: '0.3rem',
  },

  '::-webkit-scrollbar-track': {
    borderRadius: '0.5rem',
    backgroundColor: colors.GREY_200,
  },

  '::-webkit-scrollbar-thumb': {
    backgroundColor: colors.GREY_400,
    borderRadius: '0.5rem',
  },
});

export const SIZE = styleVariants({
  '5rem': [
    BASE,
    {
      height: '5rem',
    },
  ],
  '7.5rem': [
    BASE,
    {
      height: '7.5rem',
    },
  ],
  '10rem': [
    BASE,
    {
      height: '10rem',
    },
  ],
  '15rem': [
    BASE,
    {
      height: '15rem',
    },
  ],
});
