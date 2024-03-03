import { colors } from '#/style/colors';
import { style } from '@vanilla-extract/css';

export const APPLAYOUT_MOBILE = style({
  width: '100vw',
  minHeight: '100vh',
  position: 'relative',
  backgroundColor: colors.GREY_800,
});

export const INDEXLAYOUT_MOBILE = style({
  position: 'relative',
  minHeight: '100vh',
  margin: '0 auto',
  maxWidth: '462px',
  padding: '1rem',
  paddingBottom: '10rem',
  backgroundColor: colors.white,
});

export const FABLAYOUT_MOBILE = style({
  position: 'absolute',
  bottom: '1rem',
  right: '1rem',
  zIndex: '999',
  cursor: 'pointer',
});

export const TEST = style({
  width: '100%',
});
