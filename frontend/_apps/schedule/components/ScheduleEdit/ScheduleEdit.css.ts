import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({});
export const PARTICIPANTS = style({
  backgroundColor: colors.GREY_100,
  height: '2.5rem',
  width: '100%',
  borderRadius: '1rem',
});
export const AC = style({
  background: 'transparent',
  border: 'none',
});
export const SEARCHRESULT = style({
  backgroundColor: colors.white,
  height: '4rem',
  width: '100%',
  border: `1px solid ${colors.GREY_300}`,
  borderRadius: '1rem',
  padding: '0.75rem',
});
