const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  images: {
    domains: [process.env.KAKAO_CDN_DOMAIN, process.env.S3_DOMAIN],
  },
  transpilePackages: ['ui'],
  async headers() {
    return [
      {
        // matching all API routes
        source: '/api/:path*',
        headers: [
          { key: 'Access-Control-Allow-Credentials', value: 'true' },
          { key: 'Access-Control-Allow-Origin', value: '*' },
          {
            key: 'Access-Control-Allow-Methods',
            value: 'GET,OPTIONS,PATCH,DELETE,POST,PUT',
          },
          {
            key: 'Access-Control-Allow-Headers',
            value:
              'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version',
          },
        ],
      },
    ];
  },
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
