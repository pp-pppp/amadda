import { style } from '@vanilla-extract/css';
import { colors } from '#/style/colors';

export const BASE = style({
  display: 'grid',
  gridTemplateColumns: 'repeat(7, 1fr)',
  placeItems: 'center',
});
