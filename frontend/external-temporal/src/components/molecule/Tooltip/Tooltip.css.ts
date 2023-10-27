import colors from '@/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  width: '100%',
  maxWidth: '330px',
  lineHeight: '1.35',
  backgroundColor: colors.GREY_200,
  padding: '0.75rem 1rem 0.75rem 1rem',
  borderRadius: '0.66rem',
});
