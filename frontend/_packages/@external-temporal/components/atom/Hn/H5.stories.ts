import type { Meta, StoryObj } from '@storybook/react';

import { H5 } from './H5';

const meta = {
  title: 'atom/Headings/H5',
  component: H5,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H5>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 다섯 번째 제목이야',
  },
};
