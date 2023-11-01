import type { Meta, StoryObj } from '@storybook/react';

import { Spacing } from './Spacing';

const meta = {
  title: 'atom/Spacing',
  component: Spacing,
  parameters: {
    layout: 'centered',
    componentSubtitle: '여백입니다.',
    docs: {
      description: {
        component:
          'CSS 기본 속성인 margin을 대신하여 사용될 spacing입니다. 컴포넌트 간의 공간을 제어할 때 사용합니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    dir: {
      description:
        '공간이 적용될 방향입니다. h는 height를, v는 width를 의미합니다.',
    },
    size: {
      description:
        '공간의 크기입니다. 선택되는 숫자의 rem만큼 공간이 주어집니다.',
    },
  },
} satisfies Meta<typeof Spacing>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    dir: 'v',
    size: '3',
  },
};
export const Secondary: Story = {
  args: {
    dir: 'h',
    size: '1',
  },
};
