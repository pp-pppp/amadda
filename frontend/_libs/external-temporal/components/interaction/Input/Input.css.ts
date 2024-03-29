import { style, styleVariants } from '@vanilla-extract/css';
import { colors } from '#/style/colors';

const BASE = style({
  backgroundColor: colors.GREY_200,
  ':disabled': {
    backgroundColor: colors.disabled,
    cursor: 'not-allowed',
  },
});

const BASE_TEXT = style({
  border: 'none',
  borderRadius: '0.5rem',
  color: colors.BLACK,
  height: '2.5rem',
  padding: '0rem 1rem',
  width: '100%',
});
const BASE_NUMBER = style({
  border: 'none',
  borderRadius: '0.5rem',
  color: colors.BLACK,
  height: '2.5rem',
  padding: '0rem 1rem',
  width: '5.25rem',
});
const BASE_CHECKBOX = style({
  appearance: 'none',
  borderRadius: '0.3rem',
  border: `0.1rem solid ${colors.key} `,
  height: '1.25rem',
  width: '1.25rem',
  cursor: 'pointer',
});

export const TYPE = styleVariants({
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
  number: [
    BASE,
    BASE_NUMBER,
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
      '&:checked': {
        backgroundImage: 'url("/image/checked.svg")',
        backgroundSize: '1.5rem',
        backgroundPosition: 'center center',
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
