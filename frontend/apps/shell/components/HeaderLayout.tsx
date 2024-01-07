import { Header } from './Header/Header';

export default function HeaderLayout({ children }) {
  return (
    <>
      <Header />
      <main>{children}</main>
    </>
  );
}
