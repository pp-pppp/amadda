import { style } from '@vanilla-extract/css';
import colors from '#/constants/colors';

export const BASE = style({
  display: 'grid',
  gridTemplateColumns: 'repeat(7, 1fr)',
  placeItems: 'center',
  // width: '100vw',
  // maxWidth: '430px',
});
