// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import { style } from 'external-temporal';
import wrapper from '@U/store/store';

function App({ Component, pageProps }) {
  return (
    <>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </>
  );
}

export default wrapper.withRedux(App);
