import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const LAYOUT = styleVariants({
  plate: {
    width: '100vw',
    maxWidth: '430px',
    height: '3rem',
    display: 'flex',
    justifyContent: 'space-between',
    flexGrow: '1',
  },
  title: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'start',
    alignItems: 'center',
  },
  category: {
    width: '0.25rem',
    height: '3rem',
  },
  profiles: {
    width: '2rem',
    height: '2rem',
    borderRadius: '100%',
    position: 'relative',
  },
  width: {
    width: '4rem',
    textAlign: 'end',
  },
  time: {
    width: '4rem',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'end',
    justifyContent: 'end',
  },
});

export const NAME = style({
  textOverflow: 'ellipsis',
  overflow: 'hidden',
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
  WebkitLineClamp: 2,
  width: '15rem',
  lineHeight: '1.5rem',
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
