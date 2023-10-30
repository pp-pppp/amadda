import type { StorybookConfig } from '@storybook/react-webpack5';
import { VanillaExtractPlugin } from '@vanilla-extract/webpack-plugin';
import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import path from 'path';

const config: StorybookConfig = {
  stories: [
    '../../**/components/**/*.mdx',
    '../../**/components/**/*.stories.@(js|jsx|mjs|ts|tsx)',
    '../../../_packages/@external-temporal/components/**/*.mdx',
    '../../../_packages/@external-temporal/components/**/*.stories.@(js|jsx|mjs|ts|tsx)',
  ],
  addons: [
    '@storybook/addon-links',
    '@storybook/addon-essentials',
    '@storybook/addon-onboarding',
    '@storybook/addon-interactions',
  ],
  framework: {
    name: '@storybook/react-webpack5',
    options: {},
  },
  docs: {
    autodocs: 'tag',
  },
  babel: async options => ({
    ...options,
    presets: [
      ...(options.presets || []),
      [
        '@babel/preset-react',
        {
          runtime: 'automatic',
        },
        'preset-react-jsx-transform',
      ],
    ],
  }),
  staticDirs: ['../public'],
  webpackFinal(config, options) {
    // Add Vanilla-Extract and MiniCssExtract Plugins
    config.plugins?.push(
      new VanillaExtractPlugin(),
      new MiniCssExtractPlugin()
    );

    // Exclude vanilla extract files from regular css processing
    config.module?.rules?.forEach(rule => {
      if (
        typeof rule !== 'string' &&
        //@ts-ignore
        rule.test instanceof RegExp &&
        //@ts-ignore
        rule.test.test('test.css')
      ) {
        //@ts-ignore
        rule.exclude = /\.vanilla\.css$/i;
      }
    });

    config.module?.rules?.push({
      test: /\.vanilla\.css$/i, // Targets only CSS files generated by vanilla-extract
      use: [
        MiniCssExtractPlugin.loader,
        {
          loader: require.resolve('css-loader'),
          options: {
            url: false, // Required as image imports should be handled via JS/TS import statements
          },
        },
      ],
    });

    config.module?.rules?.push({
      test: /\.(ts|tsx)$/,
      use: [
        {
          loader: require.resolve('babel-loader'),
          options: {
            presets: [['react-app', { flow: false, typescript: true }]],
          },
        },
      ],
    });
    config.resolve?.extensions?.push('.ts', '.tsx');

    //@ts-ignore
    config.resolve.alias = {
      //@ts-ignore
      ...config.resolve.alias,
      '#': path.resolve(__dirname, '../../../_packages/@external-temporal/'),
    };
    return config;
  },
};
export default config;
