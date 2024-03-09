import { Header } from '../navigation/header';

export default function HeaderLayout({ children }) {
  return (
    <>
      <Header />
      <main>{children}</main>
    </>
  );
}
