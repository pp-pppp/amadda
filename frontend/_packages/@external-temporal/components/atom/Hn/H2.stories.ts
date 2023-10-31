import type { Meta, StoryObj } from '@storybook/react';

import { H2 } from './H2';

const meta = {
  title: 'atom/Headings/H2',
  component: H2,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H2>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 두 번째 제목이야',
  },
};
