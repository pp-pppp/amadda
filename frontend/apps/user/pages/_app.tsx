// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import { style } from '@amadda/external-temporal';

function App({ Component, pageProps }) {
  return (
    <>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </>
  );
}

export default App;
