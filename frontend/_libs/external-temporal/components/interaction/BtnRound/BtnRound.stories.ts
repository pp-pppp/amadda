import type { Meta, StoryObj } from '@storybook/react';

import { BtnRound } from './BtnRound';

const meta = {
  title: 'External-temporal/Interaction/Buttons/BtnRound',
  component: BtnRound,
  parameters: {
    layout: 'centered',
    componentSubtitle: '둥근 버튼입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description: '버튼의 타입을 선택합니다. form 태그 내부의 button은 submit을 선택해주세요.',
    },
    variant: {
      description: '버튼의 색상을 지정할 수 있습니다.',
    },
    size: {
      description: '버튼의 크기를 지정할 수 있습니다.',
    },
    disabled: {
      description: '버튼의 작동 여부를 정합니다. 클릭을 막고 싶다면 true를 선택해주세요.',
    },
    children: {
      description: '자식 노드를 받습니다.',
    },
    onClick: {
      description: '클릭 시 실행될 함수입니다.',
    },
  },
} satisfies Meta<typeof BtnRound>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'button',
    variant: 'key',
    disabled: false,
    size: 'M',
    children: '안녕 나는 둥근 버튼이야',
  },
};
