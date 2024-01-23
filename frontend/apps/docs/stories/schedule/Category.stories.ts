import type { Meta, StoryObj } from '@storybook/react';
import { CategoryOption } from '../../../schedule/components/CategorySelect/CategoryOption';

const meta = {
  title: 'Schedule/CategoryOption',
  component: CategoryOption,
  parameters: {
    layout: 'centered',
    componentSubtitle: '일정의 카테고리입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof CategoryOption>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: { category: { categoryColor: 'GRAY', categoryName: 'wow' } },
};
