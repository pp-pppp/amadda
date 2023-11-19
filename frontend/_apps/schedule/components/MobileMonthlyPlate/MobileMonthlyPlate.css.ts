import { style } from '@vanilla-extract/css';
import colors from '#/constants/colors';

export const BASE = style({
  width: 'calc(100vw / 7)',
  maxWidth: 'calc(430px / 7)',
  textAlign: 'center',
  height: '3.5rem',
  border: `1px solid transparent`,

  display: 'grid',
  gridTemplateRows: '1fr 1fr',
  alignItems: 'end',

  cursor: 'pointer',

  ':active': {
    border: `1px solid ${colors.key}`,
  },
});

export const SELECTED = style({
  border: `1px solid ${colors.key}`,
});
