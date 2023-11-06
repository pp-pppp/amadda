import type { Meta, StoryObj } from '@storybook/react';

import { Btn } from './Btn';

const meta = {
  title: 'External-temporal/Interaction/Buttons/Btn',
  component: Btn,
  parameters: {
    layout: 'centered',
    componentSubtitle: '사각형 버튼입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description:
        '버튼의 타입을 선택합니다. form 태그 내부의 button은 submit을 선택해주세요.',
    },
    variant: {
      description: '버튼의 색상을 지정할 수 있습니다.',
    },
    disabled: {
      description:
        '버튼의 작동 여부를 정합니다. 클릭을 막고 싶다면 true를 선택해주세요.',
    },
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
    onClick: {
      description: '클릭 시 실행될 함수입니다.',
    },
  },
} satisfies Meta<typeof Btn>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'button',
    variant: 'key',
    disabled: false,
    children: '안녕 나는 네모 버튼이야',
    onClick: (e: React.MouseEvent) => {
      console.log(e);
    },
  },
};

export const Secondary: Story = {
  args: {
    type: 'button',
    variant: 'white',
    disabled: false,
    children: '네모네모 스펀지송',
  },
};

export const Tertiary: Story = {
  args: {
    type: 'button',
    variant: 'black',
    disabled: false,
    children: '네모바지 스펀지밥',
  },
};
