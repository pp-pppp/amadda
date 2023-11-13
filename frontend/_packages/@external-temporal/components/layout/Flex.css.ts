import { style, styleVariants } from '@vanilla-extract/css';
export const BASE = style({
  width: '100%',
});
export const DIRECTION_VARIANT = styleVariants({
  row: { display: 'flex', flexDirection: 'row' },
  column: { display: 'flex', flexDirection: 'column' },
});

export const JUSTIFY_VARIANT = styleVariants({
  start: { justifyContent: 'start' },
  center: { justifyContent: 'center' },
  flexEnd: { justifyContent: 'flex-end' },
  spaceBetween: { justifyContent: 'space-between' },
  spaceAround: { justifyContent: 'space-around' },
  spaceEvenly: { justifyContent: 'space-evenly' },
});

export const ALIGN_VARIANT = styleVariants({
  start: { alignItems: 'start' },
  center: { alignItems: 'center' },
  end: { alignItems: 'end' },
  stretch: { alignItems: 'stretch' },
});

export const WRAP_VARIANT = styleVariants({
  nowrap: { flexWrap: 'nowrap' },
  wrap: { flexWrap: 'wrap' },
});
