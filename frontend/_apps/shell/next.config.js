const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  transpilePackages: ['ui'],
  webpack(config, options) {
    if (!options.isServer) {
      config.plugins.push(
        new NextFederationPlugin({
          name: 'shell',
          filename: 'static/chunks/entry.js',
          remotes: {
            user: 'user@http://localhost:3001/_next/static/chunks/entry.js',
            schedule:
              'schedule@http://localhost:3002/_next/static/chunks/entry.js',
            notice: 'notice@http://localhost:3003/_next/static/chunks/entry.js',
          },
          exposes: {},
          shared: {},
        })
      );
    }
    return config;
  },
});
