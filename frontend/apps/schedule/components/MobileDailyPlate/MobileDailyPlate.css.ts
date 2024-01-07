import { colors } from '@amadda/external-temporal';
import { style, styleVariants } from '@vanilla-extract/css';

export const LAYOUT_SCH = styleVariants({
  plate: {
    cursor: 'pointer',
    height: '3rem',
    display: 'grid',
    gridTemplateColumns: '13rem 6rem 6rem',
    width: '398px',
  },
  title: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: '12.5rem',
  },
  category: {
    width: '0.25rem',
    height: '3rem',
  },
  participants: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-around',
    alignItems: 'center',
    flexWrap: 'nowrap',
    width: '4.5rem',
  },
  profiles: {
    width: '2rem',
    height: '2rem',
    borderRadius: '100%',
    position: 'relative',
  },
  width: {
    width: '4rem',
    textAlign: 'right',
  },
  time: {
    width: '4rem',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'flex-end',
    justifyContent: 'flex-end',
  },
});

export const LAYOUT_UN = styleVariants({
  plate: {
    cursor: 'pointer',
    height: '3rem',
    display: 'grid',
    gridTemplateColumns: '19rem 6rem',
    width: '398px',
  },
  title: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: '18.5rem',
  },
  category: {
    width: '0.25rem',
    height: '3rem',
  },
  participants: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-around',
    alignItems: 'center',
    flexWrap: 'nowrap',
    width: '4.5rem',
  },
  profiles: {
    width: '2rem',
    height: '2rem',
    borderRadius: '100%',
    position: 'relative',
  },
});

export const NAME = style({
  textOverflow: 'ellipsis',
  overflow: 'hidden',
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
  WebkitLineClamp: 2,
  width: '12.5rem',
  lineHeight: '1.5rem',
});

export const CATEGORY = styleVariants({
  salmon: [LAYOUT_SCH.category, { backgroundColor: colors.category.salmon }],
  yellow: [LAYOUT_SCH.category, { backgroundColor: colors.category.yellow }],
  cyan: [LAYOUT_SCH.category, { backgroundColor: colors.category.cyan }],
  orange: [LAYOUT_SCH.category, { backgroundColor: colors.category.orange }],
  hotpink: [LAYOUT_SCH.category, { backgroundColor: colors.category.hotpink }],
  green: [LAYOUT_SCH.category, { backgroundColor: colors.category.green }],
  grey: [LAYOUT_SCH.category, { backgroundColor: colors.category.grey }],
});

export const PROFILE = styleVariants({
  0: [LAYOUT_SCH.profiles, { left: '1rem' }],
  1: [LAYOUT_SCH.profiles],
  2: [LAYOUT_SCH.profiles, { right: '1rem' }],
});

export const PERSON = style({
  fontSize: '0.75rem',
  color: colors.GREY_700,
});
