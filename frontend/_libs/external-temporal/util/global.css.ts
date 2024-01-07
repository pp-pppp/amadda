import { globalFontFace, globalStyle } from '@vanilla-extract/css';
import { colors } from '../constants/colors';

export const style = {
  overflow: globalStyle('html, body', {
    overflowX: 'hidden',
  }),

  all: globalStyle('*', {
    boxSizing: 'border-box',
    fontFamily: 'regular',
  }),

  texttag: globalStyle('html, body, h1, h2, h3, h4, h5, h6, p, span', {
    margin: 0,
    padding: 0,
    fontWeight: 400,
    fontSize: '100%',
    lineHeight: '1',
    color: colors.BLACK,
  }),

  regularFont: globalFontFace('regular', {
    src: 'url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard/packages/pretendard/dist/web/static/woff/Pretendard-Regular.woff")',
    fontWeight: 400,
  }),

  boldFont: globalFontFace('bold', {
    src: 'url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard/packages/pretendard/dist/web/static/woff/Pretendard-Bold.woff")',
    fontWeight: 700,
  }),

  atag: globalStyle('a', {
    textDecoration: 'none',
    color: 'inherit',
  }),

  listtag: globalStyle('ul, li', {
    margin: 0,
    padding: 0,
    listStyleType: 'none',
  }),

  imagetag: globalStyle('img, svg', {
    verticalAlign: 'middle',
  }),

  textarea: globalStyle('textarea', {
    fontFamily: 'pretendard',
    resize: 'none',
    margin: 0,
    padding: 0,
  }),
};
