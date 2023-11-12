// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import { style } from 'external-temporal';
import { AppLayout } from '@SH/layout/AppLayout';
import { SessionProvider } from 'next-auth/react';
import wrapper from '@SH/store/store';

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

export default wrapper.withRedux(App);
