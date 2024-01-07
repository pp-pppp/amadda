import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Card } from './Card';
import { Switch } from '#/components/interaction/Switch/Switch';
import Spacing from '#/components/typography/Spacing/Spacing';

const meta = {
  title: 'External-temporal/Views/Card',
  component: Card,
  parameters: {
    layout: 'centered',
    componentSubtitle: '여러 요소들을 넣을 수 있는 카드 컴포넌트입니다.',
    docs: {
      description: {
        component:
          '안쪽에 여러 요소들을 넣어 묶을 수 있습니다. 외곽선이 없는 카드와 있는 카드가 있고, 외곽선이 없는 경우는 padding 값도 설정되어있지 않습니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다.',
    },
  },
} satisfies Meta<typeof Card>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: [
      <>아 이것은 카드입니다. 안쪽에 자유롭게 이것저것 넣을 수 있어요.</>,
      <Spacing />,
      <Switch id="wow" selected={true} />,
      <Spacing />,
      <>이런 것도 넣을 수 있습니다</>,
    ],
  },
};
