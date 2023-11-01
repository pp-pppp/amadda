import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const CONTAINER_BASE = style({
  width: '3.75rem',
  height: '2rem',
  borderRadius: '1rem',
  display: 'flex',
  alignItems: 'center',
});
export const CONTAINER_VARIANT = styleVariants({
  unselected: {
    backgroundColor: colors.GREY_400,
  },
  selected: {
    backgroundColor: colors.key,
  },
});
export const SLIDER = style({
  position: 'relative',
  width: '3.7rem',
  height: '2rem',
  borderRadius: '1rem',
  display: 'flex',
  alignItems: 'center',
});

export const SWITCH_BASE = style({
  position: 'absolute',
  appearance: 'none',
  width: '1.5rem',
  height: '1.5rem',
  borderRadius: '0.75rem',
  backgroundColor: colors.GREY_200,
  transition: 'transform 0.2s',
});
export const SWITCH_VARIANT = styleVariants({
  unselected: {
    transform: 'translateX(0)',
  },
  selected: {
    transform: 'translateX(1.75rem)',
  },
});
