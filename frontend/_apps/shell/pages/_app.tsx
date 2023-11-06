// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import '#/util/global.css';
import { AppLayout } from '@/layout/AppLayout';

function App({ Component, pageProps }) {
  return (
    <AppLayout>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </AppLayout>
  );
}

export default App;
