import type { Meta, StoryObj } from '@storybook/react';

import { P } from './P';

const meta = {
  title: 'atom/Texts/P',
  component: P,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof P>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children:
      '안녕 나는 문단이야. 긴 텍스트가 들어가야 하면 나를 사용해야 한다고!',
  },
};
