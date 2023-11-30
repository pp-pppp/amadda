import type { Meta, StoryObj } from '@storybook/react';

import ErrorBoundary from './ErrorBoundary';

const meta = {
  title: 'External-temporal/Fallback/ErrorBoundary',
  component: ErrorBoundary,
  parameters: {
    componentSubtitle: '에러 발생 시 에러가 부모 컴포넌트로 전파되지 않도록 하는 fallback 컴포넌트입니다.',
  },
  tags: ['autodocs'],
} satisfies Meta<typeof ErrorBoundary>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
