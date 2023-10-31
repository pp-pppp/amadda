import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Segments } from './Segments';

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
  title: 'molecule/Segments',
  component: Segments,
  parameters: {
    // Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
    layout: 'centered',
    componentSubtitle:
      '여러 페이지 중 하나를 선택해 볼 수 있는 탭 컴포넌트입니다.',
    docs: {
      description: {
        component:
          '트리거 버튼에 들어갈 배열과 안쪽 페이지 컴포넌트를 배열로 전달해 사용할 수 있습니다.',
      },
    },
  },
  // This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
  tags: ['autodocs'],
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    // backgroundColor: { control: 'color' },
    titles: {
      description: '트리거 버튼에 들어갈 텍스트를 배열로 받습니다.',
      control: 'radio',
      options: [
        ['1', '2', '3'],
        ['A', 'B', 'C'],
      ],
    },
    pages: {
      description:
        '안쪽에 들어갈 페이지 배열을 받습니다. titles prop의 인덱스에 맞추어 주입합니다.',
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

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const Primary: Story = {
  args: {
    // primary: true,
    titles: ['네모', '세모', '동그라미'],
    pages: [
      <h2>'네모 페이지'</h2>,
      <h1>'세모 페이지'</h1>,
      <h3>'동그라미 페이지'</h3>,
    ],
  },
};
