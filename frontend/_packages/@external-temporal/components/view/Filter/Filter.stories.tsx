import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Filter } from './Filter';

const meta = {
  title: 'External-temporal/Views/Filter',
  component: Filter,
  parameters: {
    layout: 'centered',
    componentSubtitle: '다중 선택이 가능한 드롭다운입니다.',
    docs: {
      description: {
        component: 'dropdown 안에 checkbox를 넣었습니다. ',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    isOpen: {
      description: 'Filter 컴포넌트의 열림 또는 닫힘 여부를 선택합니다.',
    },
    onClick: {
      description: 'main을 클릭하면 작동할 함수입니다.',
    },
    main: {
      description: 'main에 보여줄 텍스트를 입력합니다.',
    },
    children: {
      description: '자식 노드를 받습니다. Filter 컴포넌트의 하위 컴포넌트인 `<Filter.FilterOption>`의 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof Filter>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    isOpen: true,
    onClick: e => console.log(e),
    main: '뷰 선택하기',
    children: [
      <Filter.FilterOption id="a" name="a" onChange={e => console.log(e.target.value)} value="a">
        a
      </Filter.FilterOption>,
      <Filter.FilterOption id="b" name="b" onChange={e => console.log(e.target.value)} value="b">
        b
      </Filter.FilterOption>,
      <Filter.FilterOption id="c" name="c" onChange={e => console.log(e.target.value)} value="c">
        c
      </Filter.FilterOption>,
    ],
  },
};
