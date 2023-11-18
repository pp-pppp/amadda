import Layout from '@SH/components/Layout';

export default function Page({ children }) {
  return <></>;
}
Page.getLayout = function getLayout(page) {
  return <Layout>{page}</Layout>;
};
