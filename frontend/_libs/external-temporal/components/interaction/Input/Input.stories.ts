/* eslint-disable @typescript-eslint/no-unused-vars */
import type { Meta, StoryObj } from '@storybook/react';

import { Input } from './Input';

const meta = {
  title: 'External-temporal/Interaction/Form/Input',
  component: Input,
  parameters: {
    componentSubtitle: 'text와 checkbox 타입을 지원하는 input 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      description: 'input의 타입을 선택할 수 있습니다.',
    },
    validator: {
      description: '사용자에게 받은 입력 받은 유효성을 검증하는 함수입니다.',
    },
    id: {
      description: 'Label 및 Caption과의 바인딩을 위한 id입니다.',
    },
    name: {
      description: '데이터 전송 시 사용되는 name입니다',
    },
    onChange: {
      description: '입력값이 변경되면 실행될 함수입니다.',
    },
    disabled: {
      description: '작동 여부를 정합니다. 입력을 막고 싶다면 true를 선택해주세요.',
    },
    placeholder: {
      description: '입력값이 없을 때 보여질 문구입니다',
    },
    value: {
      description: '입력값입니다.',
    },
    checked: {
      description: 'type이 checkbox일 때 선택여부를 지정합니다.',
    },
  },
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
