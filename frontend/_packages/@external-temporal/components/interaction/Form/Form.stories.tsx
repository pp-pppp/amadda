import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Form } from './Form';
import { Input } from '#/components/interaction/Input/Input';
import { Btn } from '#/components/interaction/Btn/Btn';

const meta = {
  title: 'External-temporal/Interaction/Form/Form',
  component: Form,
  parameters: {
    layout: 'centered',
    componentSubtitle: '폼 태그입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    formName: {
      description: '폼의 이름을 받습니다.',
    },
    onSubmit: {
      description: '폼을 전송할 때 실행될 함수를 받습니다.',
    },
    children: {
      description:
        '자식 노드를 받습니다. 폼 안쪽 요소를 자유롭게 구성할 수 있습니다.',
    },
  },
} satisfies Meta<typeof Form>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    formName: 'form',
    onSubmit: e => console.log(e),
    children: (
      <>
        <Input
          type="text"
          disabled={false}
          value="wow"
          id="abc"
          name="abc"
          onChange={e => console.log(e)}
        />
        <Btn
          type="submit"
          variant="key"
          disabled={false}
          onClick={e => console.log(e)}
        >
          wow
        </Btn>
      </>
    ),
  },
};
