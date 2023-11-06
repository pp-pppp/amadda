// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import '#/util/global.css';
import { AppLayout } from '@/layout/AppLayout';
import { SessionProvider } from 'next-auth/react';

function App({ Component, pageProps: { session, ...pageProps } }) {
  return (
    <SessionProvider session={session}>
      <AppLayout>
        {/* <Nav /> */}
        <Component {...pageProps} />
      </AppLayout>
    </SessionProvider>
  );
}

export default App;
