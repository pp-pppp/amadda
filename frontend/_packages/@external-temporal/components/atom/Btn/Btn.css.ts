import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const BASE = style({
  display: 'block',
  border: '1px solid transparent',
  borderRadius: '0.5rem',
  width: '100%',
  height: '2.5rem',
  paddingLeft: '1rem',
  paddingRight: '1rem',
  cursor: 'pointer',
});

export const VARIANTS = styleVariants({
  key: [
    BASE,
    {
      backgroundColor: colors.key,
      color: colors.white,
      '&:hover': {
        backgroundColor: colors.keyHover,
      },
      ':disabled': {
        backgroundColor: colors.disabled,
        color: colors.disalbedText,
        cursor: 'not-allowed',
      },
    },
  ],
  white: [
    BASE,
    {
      border: `1px solid ${colors.black}`,
      backgroundColor: colors.white,
      color: colors.black,
      '&:hover': {
        backgroundColor: colors.whiteHover,
      },
      ':disabled': {
        backgroundColor: colors.disabled,
        color: colors.disalbedText,
        cursor: 'not-allowed',
      },
    },
  ],
  black: [
    BASE,
    {
      backgroundColor: colors.black,
      color: colors.white,
      '&:hover': {
        backgroundColor: colors.blackHover,
      },
      ':disabled': {
        backgroundColor: colors.disabled,
        color: colors.disalbedText,
        cursor: 'not-allowed',
      },
    },
  ],
});
