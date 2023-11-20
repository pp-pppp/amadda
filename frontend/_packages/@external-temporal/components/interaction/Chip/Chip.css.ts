import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

const BASE = style({
  borderRadius: '2rem',
  width: 'fit-content',
  height: '1.75rem',
  display: 'inline-flex',
  alignItems: 'center',
  fontSize: '0.8rem',
  lineHeight: 0,
  padding: '0 0.75rem 0 0.75rem',
  marginBottom: '0.125rem',
  marginRight: '0.25rem',
  fontWeight: 400,
});

export const TYPE_VARIANT = styleVariants({
  filter: [
    BASE,
    {
      border: `1px solid ${colors.BLACK}`,
      backgroundColor: colors.white,
      color: colors.black,
      cursor: 'pointer',
    },
  ],
  filterselected: [
    BASE,
    {
      border: `1px solid transparent`,
      backgroundColor: colors.key,
      color: colors.WHITE,
      cursor: 'pointer',
    },
  ],
  keyword: [
    BASE,
    {
      color: colors.white,
      backgroundColor: colors.key,
      paddingRight: '0.4rem',
    },
  ],
  suggestion: [
    BASE,
    {
      backgroundColor: colors.key,
      color: colors.white,
      // cursor: 'pointer',
    },
  ],
});

export const SHAPE_VARIANT = styleVariants({
  round: { borderRadius: 100 },
  square: { borderRadius: '0.5rem' },
});

export const KEYWORD_DELETE = style({
  color: colors.WHITE,
  margin: 0,
  padding: 0,
  background: 'transparent',
  border: 'transparent',
});
