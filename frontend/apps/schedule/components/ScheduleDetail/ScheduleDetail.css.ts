import { colors } from '@amadda/external-temporal';
import { style } from '@vanilla-extract/css';

export const BASE = style({
  padding: '0.75rem',
  backgroundColor: colors.GREY_100,
  borderRadius: '0.5rem',
});

export const GRID = style({
  display: 'grid',
  gridTemplateColumns: '1fr 1fr 5fr',
  alignItems: 'center',
});

export const BUTTON = style({
  width: '6.5rem',
  alignContent: 'flex-end',
});
