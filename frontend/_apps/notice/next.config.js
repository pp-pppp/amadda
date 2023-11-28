const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  images: {
    domains: [process.env.KAKAO_CDN_DOMAIN, process.env.S3_DOMAIN],
  },
  transpilePackages: [
    'external-temporal',
    'connection',
    'amadda-kafka',
    'global-types',
    'tsconfig',
    'eslint-config-custom',
  ],
  basePath: '/mf/notice',
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
          name: 'notice',
          filename: 'static/chunks/entry.js',
          exposes: {
            './Notice': './pages/notice/index.tsx',
            './NoticeConfig': './pages/notice-config/index.tsx',
          },
          shared: {
            next: {
              eager: true,
              singleton: true,
              requiredVersion: '13.4.12',
            },
            '@vanilla-extract/css': {
              eager: true,
              singleton: true,
              requiredVersion: '1.13.0',
            },
          },
        })
      );
    }
    return config;
  },
});
