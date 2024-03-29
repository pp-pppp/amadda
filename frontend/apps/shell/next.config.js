// const NextFederationPlugin = require('@module-federation/nextjs-mf');
const { createVanillaExtractPlugin } = require('@vanilla-extract/next-plugin');
const withVanillaExtract = createVanillaExtractPlugin();
module.exports = withVanillaExtract({
  images: {
    domains: [process.env.KAKAO_CDN_DOMAIN, process.env.S3_DOMAIN],
  },
  transpilePackages: [
    '@amadda/external-temporal',
    '@amadda/fetch',
    '@amadda/kafka',
    '@amadda/global-types',
    '@amadda/tsconfig',
    '@amadda/eslint-config-custom',
  ],
  async headers() {
    return [
      {
        // matching all API routes
        source: '/api/:path*',
        headers: [
          { key: 'Access-Control-Allow-Credentials', value: 'true' },
          { key: 'Access-Control-Allow-Origin', value: process.env.NEXT_PUBLIC_SHELL },
          {
            key: 'Access-Control-Allow-Methods',
            value: 'GET,OPTIONS,PATCH,DELETE,POST,PUT',
          },
          {
            key: 'Access-Control-Allow-Headers',
            value: 'Authorization, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version',
          },
        ],
      },
    ];
  },
  webpack(config, options) {
    // config.infrastructureLogging = {
    //   level: 'error',
    // };
    // if (!options.isServer) {
    //   config.plugins.push(
    //     new NextFederationPlugin({
    //       name: 'shell',
    //       filename: 'static/chunks/entry.js',
    //       remotes: {
    //         user: `user@${process.env.NEXT_PUBLIC_USER}/_next/static/chunks/entry.js`,
    //         schedule: `schedule@${process.env.NEXT_PUBLIC_SCHEDULE}/_next/static/chunks/entry.js`,
    //         notice: `notice@${process.env.NEXT_PUBLIC_NOTICE}/_next/static/chunks/entry.js`,
    //       },
    //       exposes: {},
    //       shared: {
    //         next: {
    //           eager: true,
    //           singleton: true,
    //           requiredVersion: '13.4.12',
    //         },
    //         '@vanilla-extract/css': {
    //           eager: true,
    //           singleton: true,
    //           requiredVersion: '1.13.0',
    //         },
    //       },
    //     })
    //   );
    // }
    return config;
  },
});

// Injected content via Sentry wizard below

const { withSentryConfig } = require('@sentry/nextjs');

module.exports = withSentryConfig(
  module.exports,
  {
    // For all available options, see:
    // https://github.com/getsentry/sentry-webpack-plugin#options

    // Suppresses source map uploading logs during build
    silent: true,
    org: 'amadda',
    project: 'amadda-shell',
  },
  {
    // For all available options, see:
    // https://docs.sentry.io/platforms/javascript/guides/nextjs/manual-setup/

    // Upload a larger set of source maps for prettier stack traces (increases build time)
    widenClientFileUpload: true,

    // Transpiles SDK to be compatible with IE11 (increases bundle size)
    transpileClientSDK: true,

    // Routes browser requests to Sentry through a Next.js rewrite to circumvent ad-blockers (increases server load)
    tunnelRoute: '/monitoring',

    // Hides source maps from generated client bundles
    hideSourceMaps: true,

    // Automatically tree-shake Sentry logger statements to reduce bundle size
    disableLogger: true,

    // Enables automatic instrumentation of Vercel Cron Monitors.
    // See the following for more information:
    // https://docs.sentry.io/product/crons/
    // https://vercel.com/docs/cron-jobs
    automaticVercelMonitors: true,
  }
);
