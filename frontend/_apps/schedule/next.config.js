const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  domains: [process.env.KAKAO_CDN_DOMAIN, process.env.S3_DOMAIN],
  transpilePackages: ['ui'],
  basePath: '/mf/schedule',
  webpack(config, options) {
    if (!options.isServer) {
      config.plugins.push(
        new NextFederationPlugin({
          name: 'schedule',
          filename: 'static/chunks/entry.js',
          exposes: {},
          shared: {},
        })
      );
    }
    return config;
  },
});
