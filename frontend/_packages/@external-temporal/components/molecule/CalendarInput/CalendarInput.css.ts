import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const FRAME = styleVariants({
  flex: {
    display: 'flex',
  },
  cal: {
    borderRadius: '0.5rem',
    backgroundColor: `${colors.GREY_200}`,
    border: `1px solid ${colors.BLACK}`,
    padding: '0.5rem 0rem',
    width: '90%',
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(7, 1fr);',
    placeItems: 'center',
    rowGap: '0.5rem',
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
  },
});

export const DAYS = style([
  FRAME['grid'],
  {
    fontSize: '0.8rem',
  },
]);

export const DATES = style([FRAME['grid'], {}]);

export const DATE = style({
  width: '2rem',
  height: '2rem',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
});

export const SELECTED = style([
  DATE,
  {
    border: `0.1rem solid ${colors.key}`,
    borderRadius: '3rem',
    backgroundColor: `${colors.key}`,
  },
]);

export const CAL = styleVariants({
  default: { backgroundColor: `${colors.GREY_300}` },
});
