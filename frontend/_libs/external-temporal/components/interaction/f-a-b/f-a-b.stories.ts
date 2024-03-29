import type { Meta, StoryObj } from '@storybook/react';

import { FAB } from './f-a-b';

const meta = {
  title: 'External-temporal/Interaction/Buttons/FAB',
  component: FAB,
  parameters: {
    layout: 'centered',
    componentSubtitle: 'Floating Action Button입니다.',
    docs: {
      description: {
        component: '중요한 작업을 위한 디렉션을 제공하는 버튼입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof FAB>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
