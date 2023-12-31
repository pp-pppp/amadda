import { style, styleVariants } from '@vanilla-extract/css';

export const BASE = style({});

export const TYPE = styleVariants({
  text: {
    lineHeight: '1.5',
    marginBottom: '1rem',
  },
  caption: {
    fontSize: '0.8rem',
    lineHeight: '1.2',
    textAlign: 'right',
  },
});
