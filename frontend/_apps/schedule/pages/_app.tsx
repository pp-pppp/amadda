// import dynamic from 'next/dynamic';
import { lazy } from 'react';

function App({ Component, pageProps }) {
  return (
    <>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </>
  );
}

export default App;
