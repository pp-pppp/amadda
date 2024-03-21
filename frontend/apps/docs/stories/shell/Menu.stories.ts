import type { Meta, StoryObj } from '@storybook/react';
import { Menu } from '../../../shell/components/navigation/Menu';

const meta = {
  title: 'Shell/Menu',
  component: Menu,
  parameters: {
    layout: 'centered',
    componentSubtitle:
      '헤더에 들어가는 메뉴 컴포넌트입니다. 등록된 아이콘이면 모두 사용할 수 있습니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Menu>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    iconType: 'cal',
    onClick: () => {},
  },
};
