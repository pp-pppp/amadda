import type { Meta, StoryObj } from '@storybook/react';
import SignOut from '../../../shell/components/sign-out/SignOut';

const meta = {
  title: 'Shell/SignOut',
  component: SignOut,
  parameters: {
    layout: 'centered',
    componentSubtitle: '로그아웃 시 보여질 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof SignOut>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
