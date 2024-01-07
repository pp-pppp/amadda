// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import { style } from '@amadda/external-temporal';
import useSWR from 'swr';
import { SWRConfig } from 'swr';
import { APPLAYOUT_MOBILE } from '@SCH/layout/Layout.css';

function App({ Component, pageProps }) {
  return (
    <SWRConfig>
      <div className={APPLAYOUT_MOBILE}>
        {/* <Nav /> */}
        <Component {...pageProps} />
      </div>
    </SWRConfig>
  );
}

export default App;
