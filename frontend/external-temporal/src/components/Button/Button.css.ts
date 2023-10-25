import { style } from '@vanilla-extract/css';
// import colors from '../../../src/constants/colors';
import colors from '@/constants/colors';

export const base = style({
  color: colors.key,
  fontWeight: 400,
  backgroundColor: 'white',
  border: '1px solid black',
  padding: '0.75rem',
  borderRadius: '1rem',
  cursor: 'pointer',
});

export const test = style({
  color: colors.key,
  fontWeight: 700,
  backgroundColor: 'white',
  border: '1px solid black',
  padding: '0.75rem',
  borderRadius: '1rem',
  cursor: 'pointer',
});
