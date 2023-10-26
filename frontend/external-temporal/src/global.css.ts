import { globalFontFace, globalStyle } from '@vanilla-extract/css';
import colors from './constants/colors';

globalFontFace('pretendard', {
  src: 'url("/fonts/Pretendard-Regular.woff")',
  fontWeight: 400,
});

globalFontFace('pretendardBold', {
  src: 'url("/fonts/Pretendard-Bold.woff")',
  fontWeight: 700,
});

globalStyle('html, body, h1, h2, h3, h4, h5, h6, p, span', {
  fontFamily: 'pretendard',
  margin: 0,
  padding: 0,
  fontWeight: 400,
  fontSize: '100%',
  lineHeight: '1',
  color: colors.BLACK,
});

globalStyle('*', {
  boxSizing: 'border-box',
});

globalStyle('a', {
  textDecoration: 'none',
  color: 'inherit',
});

globalStyle('ul, li', {
  margin: 0,
  padding: 0,
  listStyleType: 'none',
});

globalStyle('img, svg', {
  verticalAlign: 'middle',
});

globalStyle('textarea', {
  fontFamily: 'pretendard',
  resize: 'none',
  margin: 0,
  padding: 0,
});
