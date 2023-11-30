import type { Meta, StoryObj } from '@storybook/react';

import { P } from './P';

const meta = {
  title: 'External-temporal/Typography/Texts/P',
  component: P,
  parameters: {
    layout: 'centered',
    componentSubtitle: '문단 입력 시 사용하는 P 태그입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description: '종류를 선택합니다. text는 일반 문단을, caption은 input의 하단에 보여질 caption을 의미합니다.',
    },
    color: {
      description: '글자의 색상을 선택할 수 있습니다..',
    },
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof P>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'text',
    children: '안녕 나는 문단이야. 긴 텍스트가 들어가야 하면 나를 사용해야 한다고!',
  },
};
