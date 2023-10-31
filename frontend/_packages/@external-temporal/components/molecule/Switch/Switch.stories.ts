import type { Meta, StoryObj } from '@storybook/react';

import { Switch } from './Switch';

const meta = {
  title: 'molecule/Switch',
  component: Switch,
  parameters: {
    layout: 'centered',
    componentSubtitle: '한 줄 요약',
    docs: {
      description: {
        component: '컴파운드 컴포넌트인 경우 여기서 설명합니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Switch>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    id: 'id',
    selected: true,
    value: 'wow',
  },
};
