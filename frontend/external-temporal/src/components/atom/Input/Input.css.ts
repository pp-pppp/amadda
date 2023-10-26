import { style, styleVariants } from '@vanilla-extract/css';
import colors from '@/constants/colors';

const BASE = style({
  height: '2.5rem',
  borderRadius: '0.5rem',
  backgroundColor: colors.GREY_200,
  ':disabled': {
    backgroundColor: colors.disabled,
    cursor: 'not-allowed',
  },
});

const BASE_TEXT = style({
  color: colors.BLACK,
  border: 'none',
  padding: '0rem 1rem',
  width: '100%',
});

const BASE_CHECKBOX = style({
  appearance: 'none',
  border: `0.1rem solid ${colors.key} `,
  width: '2.5rem',
  cursor: 'pointer',
});

export const TYPE_VARIANTS = styleVariants({
  text: [
    BASE,
    BASE_TEXT,
    {
      ':focus': {
        outline: `0.1rem solid ${colors.key}`,
      },
      '::placeholder': {
        color: colors.GREY_500,
      },
      ':disabled': {
        color: colors.disalbedText,
      },
    },
  ],
  checkbox: [
    BASE,
    BASE_CHECKBOX,
    {
      ':checked': {
        backgroundColor: colors.key,
      },
      ':disabled': {
        border: 'none',
        '&:checked': {
          backgroundColor: colors.keyHover,
        },
      },
    },
  ],
});
