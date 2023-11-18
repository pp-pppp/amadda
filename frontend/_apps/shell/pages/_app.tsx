import { style } from 'external-temporal';
import { AppLayout } from '@SH/layout/AppLayout';
import { SessionProvider } from 'next-auth/react';
import HeaderLayout from '@SH/components/HeaderLayout';

function App({ Component, pageProps: { session, ...pageProps } }) {
  return (
    <SessionProvider session={session}>
      <AppLayout>
        <IndexLayout>
          <HeaderLayout>
            <Component {...pageProps} />
          </HeaderLayout>
        </IndexLayout>
      </AppLayout>
    </SessionProvider>
  );
}

export default App;
