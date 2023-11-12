// import dynamic from 'next/dynamic';
import { lazy } from 'react';
import { style } from 'external-temporal';
import { AppLayout } from '@/layout/AppLayout';
import { SessionProvider } from 'next-auth/react';
import wrapper from '@/store/store';

function App({ Component, pageProps: { session, ...pageProps } }) {
  const { store, props } = wrapper.useWrappedStore(pageProps);
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
