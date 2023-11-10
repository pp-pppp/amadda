// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import '#/util/global.css';
import wrapper from '@/store/store';

function App({ Component, pageProps }) {
  return (
    <>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </>
  );
}

export default wrapper.withRedux(App);
