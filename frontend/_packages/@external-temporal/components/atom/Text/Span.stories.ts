import type { Meta, StoryObj } from '@storybook/react';

import { Span } from './Span';

const meta = {
  title: 'atom/Texts/Span',
  component: Span,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Span>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 제목이야',
  },
};
