import colors from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

export const UL_BASE = style({
  listStyleType: 'none',
});
export const UL_DISPLAY_VARIANT = styleVariants({
  inlineBlock: {
    display: 'inline-block',
  },
  block: {
    display: 'block',
  },
  inline: {
    display: 'inline',
  },
  inlineFlex: {
    display: 'inline-flex',
    flexDirection: 'row',
  },
  Flex: {
    display: 'flex',
  },
  FlexColumn: {
    display: 'flex',
    flexDirection: 'column',
  },
});
export const LI_DISPLAY_VARIANT = styleVariants({
  inlineBlock: {
    display: 'inline-block',
  },
  block: {
    display: 'block',
  },
  inline: {
    display: 'inline',
  },
});
