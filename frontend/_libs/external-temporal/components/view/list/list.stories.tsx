import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { List } from './lList';

const meta = {
  title: 'External-temporal/Views/List',
  component: List,
  parameters: {
    layout: 'centered',
    componentSubtitle: '리스트 컴포넌트입니다. 기본적으로 세로로 항목을 나열합니다.',
    docs: {
      description: {
        component:
          '`<List>` 안에서 `<List.Ul>`, `<List.Li>`를 사용해 기존 마크업처럼 사용할 수 있습니다. 리스트의 제목이 필요한 경우에는 `<List>`의 `title` prop으로 제목을 입력할 수 있습니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다.',
    },
  },
} satisfies Meta<typeof List>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: [
      <>
        <List.Ul>
          <List.Li>우와</List.Li>
          <List.Li>우와</List.Li>
          <List.Li>우와</List.Li>
        </List.Ul>
      </>,
    ],
  },
};
