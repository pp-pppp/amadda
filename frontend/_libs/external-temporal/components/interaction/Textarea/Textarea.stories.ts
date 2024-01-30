import type { Meta, StoryObj } from '@storybook/react';

import { Textarea } from './Textarea';

const meta = {
  title: 'External-temporal/Interaction/Input/Textarea',
  component: Textarea,
  parameters: {
    layout: 'centered',
    componentSubtitle: 'input 보다 긴 입력값을 받는 Textarea입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    height: {
      description: '입력창의 높이를 선택할 수 있습니다.',
    },
    placeholder: {
      description: '입력값이 없을 때 보여질 문구입니다',
    },
    value: {
      description: '입력값입니다.',
    },
  },
} satisfies Meta<typeof Textarea>;

export default meta;
type Story = StoryObj<typeof meta>;

export const None: Story = {
  args: {
    value: `1. 동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려 강산 대한 사람 대한으로 길이 보전하세
    2. 남산 위에 저 소나무 철갑을 두른 듯 바람 서리 불변함은 우리 기상일세 무궁화 삼천리 화려 강산 대한 사람 대한으로 길이 보전하세
    3. 가을 하늘 공활한데 높고 구름 없이 밝은 달은 우리 가슴 일편단심일세 무궁화 삼천리 화려 강산 대한 사람 대한으로 길이 보전하세
    4. 이 기상과 이 맘으로 충성을 다하여 괴로우나 즐거우나 나라 사랑하세 무궁화 삼천리 화려 강산 대한 사람 대한으로 길이 보전하세`,
    placeholder: '',
    height: '5rem',
  },
};
