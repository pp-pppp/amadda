import { Header } from '../navigation/Header';

export default function HeaderLayout({ children }) {
  return (
    <>
      <Header />
      <main>{children}</main>
    </>
  );
}
