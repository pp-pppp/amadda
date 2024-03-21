import { colors } from '#/style/colors';
import { style, styleVariants } from '@vanilla-extract/css';
export const SEGMENT_CONTAINER = style({
  display: 'flex',
  flexDirection: 'column',
  width: '100%',
});
export const TRIGGER_CONTAINER = style({
  marginLeft: 'auto',
});
const TRIGGERS = style({
  display: 'flex',
  flexDirection: 'row',
});
const TRIGGER_BASE = style({
  padding: '0.5rem 0.85rem 0.5rem 0.85rem',
  cursor: 'pointer',
  boxSizing: 'border-box',
  ':first-of-type': {
    borderTopLeftRadius: '0.5rem',
    borderBottomLeftRadius: '0.5rem',
  },
  ':last-of-type': {
    borderTopRightRadius: '0.5rem',
    borderBottomRightRadius: '0.5rem',
  },
});
export const TRIGGER_VARIANT = styleVariants({
  selected: [
    TRIGGER_BASE,
    {
      backgroundColor: colors.key,
      color: colors.white,
      border: `1px solid ${colors.key}`,
      borderLeft: '1px solid transparent',
    },
  ],
  unselected: [
    TRIGGER_BASE,
    {
      backgroundColor: colors.white,
      border: `1px solid ${colors.GREY_300}`,
      borderLeft: '1px solid transparent',
      ':first-of-type': {
        borderLeft: `1px solid ${colors.GREY_300}`,
      },
    },
  ],
});
export const CONTENTS = style({});
