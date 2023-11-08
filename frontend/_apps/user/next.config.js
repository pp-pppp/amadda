const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  transpilePackages: ['ui'],
  basePath: '/mf/user',
  webpack(config, options) {
    if (!options.isServer) {
      config.plugins.push(
        new NextFederationPlugin({
          name: 'user',
          filename: 'static/chunks/entry.js',
          exposes: {},
          shared: {},
        })
      );
    }
    return config;
  },
});
