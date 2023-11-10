import { style } from '@vanilla-extract/css';
import colors from '#/constants/colors';

export const BASE = style({
  width: '3rem',
  textAlign: 'center',
  height: '3.5rem',

  display: 'grid',
  gridTemplateRows: '1fr 1fr',
  alignItems: 'end',
});

export const SELECTED = style({
  border: `2px solid ${colors.keyHover}`,
});
