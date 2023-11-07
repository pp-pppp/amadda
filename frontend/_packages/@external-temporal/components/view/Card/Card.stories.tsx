import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Card } from './Card';

const meta = {
  title: 'External-temporal/Views/Card',
  component: Card,
  parameters: {
    layout: 'centered',
    componentSubtitle: '다중 선택이 가능한 드롭다운입니다.',
    docs: {
      description: {
        component: 'dropdown 안에 checkbox를 넣었습니다. ',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description:
        '자식 노드를 받습니다. 기본적으로 블록 요소이므로 모든 다른 블록 요소를 사용할 수 있습니다.',
    },
  },
} satisfies Meta<typeof Card>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: [<>abcd</>],
  },
};
