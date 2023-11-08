import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  backgroundColor: colors.white,
  width: 'auto',
});
export const BORDER = style({
  padding: '0.75rem',
  border: `1px solid ${colors.key}`,
  borderRadius: '0.5rem',
});
