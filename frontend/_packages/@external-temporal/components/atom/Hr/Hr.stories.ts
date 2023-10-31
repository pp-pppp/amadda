import type { Meta, StoryObj } from '@storybook/react';

import { Hr } from './Hr';

const meta = {
  title: 'atom/Hr',
  component: Hr,
  parameters: {
    componentSubtitle: '구분선',
    docs: {
      description: {
        component: '커스텀한 구분선입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Hr>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
