import type { Meta, StoryObj } from '@storybook/react';
import { Header } from '../../../shell/components/navigation/Header';

const meta = {
  title: 'Shell/Header',
  component: Header,
  parameters: {
    layout: 'centered',
    componentSubtitle:
      '모바일 헤더 컴포넌트입니다. 전체 레이아웃에서 작동합니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Header>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
