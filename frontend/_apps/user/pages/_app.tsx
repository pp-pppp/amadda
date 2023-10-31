// import dynamic from 'next/dynamic';
import { lazy } from "react";
import "#/util/global.css";

function App({ Component, pageProps }) {
  return (
    <>
      {/* <Nav /> */}
      <Component {...pageProps} />
    </>
  );
}

export default App;
