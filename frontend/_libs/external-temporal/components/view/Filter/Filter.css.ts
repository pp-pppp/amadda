import { colors } from '#/constants/colors';
import { style, styleVariants } from '@vanilla-extract/css';

// 기본 CSS
const BASE = style({
  width: '10rem',
});

// 배치 관련 CSS
const FRAME = style({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
});

// 배경 색상
const BGCOLOR = style({
  backgroundColor: colors.GREY_200,
});

// 상태 및 위치에 따른 styleVariants
export const STATUS = styleVariants({
  // 최상위 div : 열린 상태
  open: [
    BASE,
    BGCOLOR,
    {
      position: 'absolute',
      right: '1rem',
      top: '7.88rem',
      height: 'fit-content',
      borderRadius: '0.75rem',
      outline: `0.1rem solid ${colors.key}`,
      paddingBottom: '0.25rem',
    },
  ],

  // 최상위 div : 닫힌 상태
  close: [
    BASE,
    {
      position: 'absolute',
      right: '1rem',
      top: '7.88rem',
      height: 'fit-content',
      border: 'none',
    },
  ],

  // main(뷰 선택하기) div : 열린 상태
  openMain: [
    BASE,
    FRAME,
    BGCOLOR,
    {
      position: 'relative',
      borderRadius: '0.75rem',
      padding: '0.5rem',
    },
  ],

  // main(뷰 선택하기) div : 닫힌 상태
  closeMain: [
    BASE,
    FRAME,
    BGCOLOR,
    {
      borderRadius: '0.5rem',
      padding: '0.5rem',
      ':hover': {
        outline: `0.1rem solid ${colors.key}`,
      },
    },
  ],

  // ul : 열린 상태
  openOptions: [
    BASE,
    FRAME,
    BGCOLOR,
    {
      borderRadius: '0.75rem',
      flexDirection: 'column',
      zIndex: '999',
      position: 'relative',
    },
  ],

  // ul : 닫힌 상태
  closeOptions: [
    BASE,
    FRAME,
    {
      display: 'none',
    },
  ],

  // li css
  option: [
    BASE,
    FRAME,
    {
      padding: '0.5rem',
      position: 'relative',
    },
  ],
});
