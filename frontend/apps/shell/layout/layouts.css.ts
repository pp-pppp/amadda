import { colors } from '#/style/colors';
import { style } from '@vanilla-extract/css';

export const APPLAYOUT_MOBILE = style({
  width: '100vw',
  minHeight: '100vh',
  position: 'relative',
  backgroundColor: colors.GREY_800,
});

export const INDEXLAYOUT_MOBILE = style({
  // position: 'absolute',
  // width: '90%',
  // left: '50%',
  // top: '50%',
  // transform: 'translate(-50%, -50%)',

  position: 'relative',
  minHeight: '100vh',
  margin: '0 auto',
  maxWidth: '462px',
  padding: '1rem',
  paddingBottom: '10rem',
  backgroundColor: colors.white,
});

export const FABLAYOUT_MOBILE = style({
  position: 'fixed',
  bottom: '1rem',
  right: 'calc(50vw - 223px)',
  zIndex: '999',
  cursor: 'pointer',
});

export const MAIN = style({
  position: 'absolute',
  margin: '0 auto',
  width: '90%',
  left: '50%',
  top: '50%',
  transform: 'translate(-50%, -50%)',

  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  // height: '100vh',
});
