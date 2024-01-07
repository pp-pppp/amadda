import type { Meta, StoryObj } from '@storybook/react';

import { Switch } from './Switch';

const meta = {
  title: 'External-temporal/Interaction/Input/Switch',
  component: Switch,
  parameters: {
    layout: 'centered',
    componentSubtitle: '선택 여부를 알 수 있는 스위치입니다.',
    docs: {
      description: {
        component: 'input의 checkbox로 구현되었으며 기본 로직은 checkbox와 유사합니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    id: {
      description: 'input 태그의 id값입니다.',
    },
    selected: {
      description: '스위치의 선택 여부입니다. true 값이 선택이 된 상태를 의미합니다.',
    },
  },
} satisfies Meta<typeof Switch>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    id: 'id',
    selected: true,
  },
};
