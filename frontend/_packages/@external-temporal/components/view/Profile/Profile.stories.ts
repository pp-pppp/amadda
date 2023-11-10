import type { Meta, StoryObj } from '@storybook/react';
import { Profile } from './Profile';

const meta = {
  title: 'View/Profile',
  component: Profile,
  parameters: {
    layout: 'centered',
    componentSubtitle:
      '유저 프로필 이미지입니다. Next/Image가 내부적으로 사용되어 Next.js에서만 사용할 수 있습니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Profile>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    src: '',
    alt: 'empty image',
  },
};
