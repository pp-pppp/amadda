import type { Meta, StoryObj } from '@storybook/react';

import { Loading } from './Loading';

const meta = {
  title: 'External-temporal/Fallback/Loading',
  component: Loading,
  parameters: {
    componentSubtitle: '로딩 컴포넌트입니다.',
  },
  tags: ['autodocs'],
} satisfies Meta<typeof Loading>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
