import colors from '#/constants/colors';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  padding: '0.75rem',
  backgroundColor: colors.GREY_200,
  borderRadius: '0.5rem',
});

export const GRID = style({
  display: 'grid',
  gridTemplateColumns: '1fr 1fr 5fr',
  alignItems: 'center',
});

export const BUTTON = style({
  width: '6.5rem',
  alignContent: 'end',
});
