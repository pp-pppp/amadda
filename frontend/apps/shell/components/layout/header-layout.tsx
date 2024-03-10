import { Navigation } from '../navigation/navigation';

export default function HeaderLayout({ children }) {
  return (
    <>
      <Navigation />
      <main>{children}</main>
    </>
  );
}
