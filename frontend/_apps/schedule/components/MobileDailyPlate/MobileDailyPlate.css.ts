import colors from '#/constants/colors';
import { styleVariants } from '@vanilla-extract/css';

export const LAYOUT = styleVariants({
  plate: {
    width: '100%',
    height: '2rem',
    display: 'flex',
    justifyContent: 'space-between',
    flexGrow: '1',
  },
  category: {
    width: '0.25rem',
    height: '2rem',
  },
  profiles: {
    width: '2rem',
    height: '2rem',
    borderRadius: '100%',
    position: 'relative',
  },
  width: {
    width: '4rem',
  },
  time: {
    width: '4rem',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'end',
    justifyContent: 'end',
  },
});

export const CATEGORY = styleVariants({
  salmon: [LAYOUT.category, { backgroundColor: colors.category.salmon }],
  yellow: [LAYOUT.category, { backgroundColor: colors.category.yellow }],
  cyan: [LAYOUT.category, { backgroundColor: colors.category.cyan }],
  orange: [LAYOUT.category, { backgroundColor: colors.category.orange }],
  hotpink: [LAYOUT.category, { backgroundColor: colors.category.hotpink }],
  green: [LAYOUT.category, { backgroundColor: colors.category.green }],
  grey: [LAYOUT.category, { backgroundColor: colors.category.grey }],
});

export const PROFILE = styleVariants({
  0: [LAYOUT.profiles, { left: '2rem' }],
  1: [LAYOUT.profiles, { left: '1rem' }],
  2: [LAYOUT.profiles],
});
