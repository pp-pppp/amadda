import type { Preview } from '@storybook/react';
import React from 'react';
import '#/util/global.css';

const preview: Preview = {
  parameters: {
    actions: { argTypesRegex: '^on[A-Z].*' },
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
  decorators: [
    Story => (
      <div style={{ minWidth: '430px' }}>
        <Story />
      </div>
    ),
  ],
};

export default preview;
