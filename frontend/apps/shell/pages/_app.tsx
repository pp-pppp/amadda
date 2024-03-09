import { style } from '@amadda/external-temporal';
import { AppLayout } from '@/layout/app-layout';
import { IndexLayout } from '@/layout/index-layout';

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
