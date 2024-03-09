import type { Meta, StoryObj } from '@storybook/react';

import { Icon } from './icon';

const meta = {
  title: 'External-temporal/Views/Icon',
  component: Icon,
  parameters: {
    componentSubtitle: 'svg로 구성된 아이콘입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description: '사용하려는 아이콘을 선택할 수 있습니다.',
    },
    color: {
      description: '아이콘의 색상을 선택할 수 있습니다.',
    },
    cursor: {
      description: '커서 종류를 선택할 수 있습니다. 기본은 화살표로, pointer를 선택하면 손가락 모양으로 커서가 변경됩니다.',
    },
  },
} satisfies Meta<typeof Icon>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'cal',
    color: 'black',
  },
};
export const Secondary: Story = {
  args: {
    type: 'friends',
    color: 'key',
  },
};
