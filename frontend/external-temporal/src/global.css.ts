import { globalFontFace, globalStyle } from '@vanilla-extract/css';

globalFontFace('pretendard', {
  src: 'url("/fonts/Pretendard-Regular.woff")',
  fontWeight: 400,
});

globalFontFace('pretendardBold', {
  src: 'url("/fonts/Pretendard-Bold.woff")',
  fontWeight: 700,
});

globalStyle('html, body', {
  backgroundColor: 'pink',
});
