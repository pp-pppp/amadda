const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  images: {
    domains: [process.env.KAKAO_CDN_DOMAIN],
  },
  transpilePackages: ['ui'],
  webpack(config, options) {
    if (!options.isServer) {
      config.plugins.push(
        new NextFederationPlugin({
          name: 'shell',
          filename: 'static/chunks/entry.js',
          remotes: {
            user: `user@${process.env.NEXT_PUBLIC_USER}/_next/static/chunks/entry.js`,
            schedule: `schedule@${process.env.NEXT_PUBLIC_SCHEDULE}/_next/static/chunks/entry.js`,
            notice: `notice@${process.env.NEXT_PUBLIC_NOTICE}/_next/static/chunks/entry.js`,
          },
          exposes: {},
          shared: {},
        })
      );
    }
    return config;
  },
});
