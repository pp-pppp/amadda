import { style } from '@vanilla-extract/css';

export const GRID = style({
  display: 'grid',
  gridTemplateRows: 'repeat(6, 1fr)',
  gridTemplateColumns: 'repeat(7, 1fr)',
  placeItems: 'center',
  // width: '100vw',
  // maxWidth: '430px',
});
