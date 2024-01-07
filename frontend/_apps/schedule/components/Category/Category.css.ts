import { colors } from 'external-temporal';
import { style, styleVariants } from '@vanilla-extract/css';

export const CONTAINER = style({
  height: '3rem',
  width: '100%',
  borderRadius: '1rem',
  padding: '0.5rem',
  display: 'flex',
  alignItems: 'center',
  background: colors.GREY_100,
});
export const BASE = style({
  padding: '0.25rem 0.5rem 0.25rem 0.5rem',
  borderRadius: '0.5rem',
  fontSize: '0.85rem',
});
export const COLOR_VARIANTS = styleVariants({
  SALMON: [
    BASE,
    {
      color: colors.category.salmon,
      border: `1px solid ${colors.category.salmon}`,
    },
  ],
  YELLOW: [
    BASE,
    {
      color: colors.category.yellow,
      border: `1px solid ${colors.category.yellow}`,
    },
  ],
  CYAN: [
    BASE,
    {
      color: colors.category.cyan,
      border: `1px solid ${colors.category.cyan}`,
    },
  ],
  ORANGE: [
    BASE,
    {
      color: colors.category.orange,
      border: `1px solid ${colors.category.orange}`,
    },
  ],
  HOTPINK: [
    BASE,
    {
      color: colors.category.hotpink,
      border: `1px solid ${colors.category.hotpink}`,
    },
  ],
  GREEN: [
    BASE,
    {
      color: colors.category.green,
      border: `1px solid ${colors.category.green}`,
    },
  ],
  GRAY: [
    BASE,
    {
      color: colors.category.grey,
      border: `1px solid ${colors.category.grey}`,
    },
  ],
});
