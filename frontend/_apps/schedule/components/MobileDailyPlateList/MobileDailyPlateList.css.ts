import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  width: '100vw',
  maxWidth: '430px',
  borderTop: `1px solid ${colors.GREY_300} `,
  paddingTop: '1rem',
});
