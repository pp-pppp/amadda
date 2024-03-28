import { colors } from '#/style/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const GROUP_LAYOUT = style({
  borderBottom: `1px solid ${colors.GREY_200}`,
  padding: '0.5rem 0 0.5rem 0',
});
export const GROUP_TITLE = style({
  cursor: 'pointer',
});
export const GROUP_NAME = style({
  color: colors.GREY_600,
});
export const GROUP_NAME_CHANGE = style({
  background: 'transparent',
  border: '0px solid transparent',
  borderBottom: `1px solid ${colors.GREY_600}`,
  padding: 0,
  margin: 0,
});
export const FOLD_VARIANT = styleVariants({
  fold: { display: 'none' },
  unfold: { display: 'block' },
});
export const TOGGLE = style({
  width: '100%',
  cursor: 'pointer',
  height: '1.25rem',
  display: 'block',
  border: 'transparent',
  background: 'transparent',
  padding: '0',
  margin: '0 0 0.5rem 0',
});

const GROUP_EDIT_BASE = style({
  border: `1px solid transparent`,
  textAlign: 'right',
  cursor: 'pointer',
  borderRadius: '1rem',
  padding: '0.3rem 0.5rem 0.3rem 0.5rem',
});
export const GROUP_EDIT = styleVariants({
  EDIT: [
    GROUP_EDIT_BASE,
    {
      background: colors.GREY_200,
    },
  ],
  SAVE: [
    GROUP_EDIT_BASE,
    {
      background: colors.key,
      color: colors.white,
    },
  ],
  DELETE: [
    GROUP_EDIT_BASE,
    {
      background: colors.category.hotpink,
      color: colors.white,
    },
  ],
});
