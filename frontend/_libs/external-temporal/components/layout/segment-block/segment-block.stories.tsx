import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Segments } from './segment-block';

const meta = {
  title: 'External-temporal/Layout/Segments',
  component: Segments,
  parameters: {
    layout: 'centered',
    componentSubtitle: '여러 페이지 중 하나를 선택해 볼 수 있는 탭 컴포넌트입니다.',
    docs: {
      description: {
        component: '트리거 버튼에 들어갈 배열과 안쪽 페이지 컴포넌트를 배열로 전달해 사용할 수 있습니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    titles: {
      description: '트리거 버튼에 들어갈 텍스트를 배열로 받습니다.',
      control: 'radio',
      options: [
        ['1', '2', '3'],
        ['A', 'B', 'C'],
      ],
    },
    pages: {
      description: '안쪽에 들어갈 페이지 배열을 받습니다. titles prop의 인덱스에 맞추어 주입합니다.',
      control: 'radio',
      options: [
        ['page1', 'page2', 'page3'],
        ['pageA', 'pageB', 'pageC'],
      ],
    },
  },
} satisfies Meta<typeof Segments>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    titles: ['네모', '세모', '동그라미'],
    pages: [<h2>'네모 페이지'</h2>, <h1>'세모 페이지'</h1>, <h3>'동그라미 페이지'</h3>],
  },
};
