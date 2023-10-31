import React from "react";
import type { Meta, StoryObj } from "@storybook/react";

import { Filter } from "./Filter";

const meta = {
  title: "molecule/Filter",
  component: Filter,
  parameters: {
    layout: "centered",
    componentSubtitle: "다중 선택이 가능한 드롭다운",
    docs: {
      description: {
        component: "dropdown 안에 checkbox를 넣었습니다. ",
      },
    },
  },
  tags: ["autodocs"],
  argTypes: {
    multiselect: {
      description: "다중선택 여부를 선택합니다.",
    },
    isOpen: {
      description: "Filter 컴포넌트의 열림 또는 닫힘 여부를 선택합니다.",
    },
    children: {
      description:
        "자식 노드를 받습니다. Filter 컴포넌트의 하위 컴포넌트인 '<Filter.FilterOption>의 사용을 권고합니다.",
    },
  },
} satisfies Meta<typeof Filter>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    multiselect: true,
    isOpen: true,
    children: [
      <Filter.FilterOption>a</Filter.FilterOption>,
      <Filter.FilterOption>b</Filter.FilterOption>,
      <Filter.FilterOption>c</Filter.FilterOption>,
    ],
  },
};
