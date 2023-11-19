/* eslint-disable @typescript-eslint/no-unused-vars */
import type { Meta, StoryObj } from '@storybook/react';

import { Chip } from './Chip';

const meta = {
  title: 'External-temporal/Interaction/Chip',
  component: Chip,
  parameters: {
    layout: 'centered',
    componentSubtitle: '다양한 곳에 쓸 수 있는 Chip 컴포넌트입니다.',
    docs: {
      description: {
        component:
          '필터로 사용되는 칩(Filter), 사용자를 할당하거나 할당된 사용자를 취소할 때 사용하는 칩(Keyword), 키워드나 해시태그를 표시할 때 사용되는 칩(Suggestion)이 있습니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description: 'Chip의 타입을 정합니다.',
    },
    shape: {
      description: '모양을 정할 수 있습니다.',
    },
    label: {
      description: '바인딩 될 input 태그의 id입니다.',
    },
    onFlitered: {
      description: '필터 칩이 선택되었을 때 해당 값을 함수로 전달합니다.',
    },
    onDelete: {
      description:
        '키워드 칩이 생성된 상태에서 X버튼을 누르면 해당 값을 함수로 전달합니다.',
    },
  },
} satisfies Meta<typeof Chip>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Keyword: Story = {
  args: {
    type: 'keyword',
    label: '나는 참가자 칩이야',
    shape: 'square',
  },
};
export const Filter: Story = {
  args: {
    type: 'filter',
    label: '나는 필터 칩이야.',
    shape: 'round',
  },
};
