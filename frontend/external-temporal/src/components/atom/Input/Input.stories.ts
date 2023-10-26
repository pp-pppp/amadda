/* eslint-disable @typescript-eslint/no-unused-vars */
import type { Meta, StoryObj } from '@storybook/react';

import Input from './Input';

const meta = {
  title: 'atom/Input',
  component: Input,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Input>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Text: Story = {
  args: {
    type: 'text',
    id: 'sample-id',
    name: ' sample-input',
    onChange: e => {},
    disabled: false,
    placeholder: '이메일을 입력해주세요.',
    value: 'khappy517@naver.com',
  },
};

export const Checkbox: Story = {
  args: {
    type: 'checkbox',
    id: 'sample-id',
    name: ' sample-input',
    onChange: e => {},
    disabled: false,
    checked: false,
    value: 'true',
  },
};
