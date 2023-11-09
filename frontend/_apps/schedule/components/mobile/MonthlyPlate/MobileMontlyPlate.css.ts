import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  width: '3rem',
  height: '3rem',
  display: 'flex',
  alignItems: 'center',
});

export const SELECTED = style({
  border: `0.5px solid ${colors.key}`,
});
