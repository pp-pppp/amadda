import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { Filter } from './Filter';

const meta = {
  title: 'molecule/Filter',
  component: Filter,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Filter>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    multiselect: true,
    isOpen: true,
    children: [<Filter.FilterOption>a</Filter.FilterOption>,<Filter.FilterOption>b</Filter.FilterOption>,<Filter.FilterOption>c</Filter.FilterOption>]
  },
};
