import { style } from 'external-temporal';
import { AppLayout } from '@SH/layout/AppLayout';
import { IndexLayout } from '@SH/layout/IndexLayout';

function App({ Component, pageProps: { ...pageProps } }) {
  return (
    <AppLayout>
      <IndexLayout>
        <Component {...pageProps} />
      </IndexLayout>
    </AppLayout>
  );
}

export default App;
