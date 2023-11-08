import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const UL_BASE = style({
  listStyleType: 'none',
});
export const LI_BASE = style({
  display: 'block',
  padding: '0.75rem',
  backgroundColor: colors.GREY_100,
  width: 'auto',
  marginBottom: '0.5rem',
  borderRadius: '1rem',
});
