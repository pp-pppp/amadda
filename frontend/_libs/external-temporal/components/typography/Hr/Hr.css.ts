import { colors } from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  width: '100%',
  height: '2rem',
  border: 'none',
  borderTop: `2px solid ${colors.GREY_200}`,
});
