import { style } from '@vanilla-extract/css';

export const APPLAYOUT_MOBILE = style({
  margin: '0 auto',
  width: '430px',
  height: '100vh',
  position: 'relative',
});

export const INDEXLAYOUT_MOBILE = style({
  position: 'absolute',
  left: '50%',
  top: '50%',
  transform: 'translate(-50%, -50%)',
});
