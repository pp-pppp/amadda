import React, { HTMLAttributes } from "react";
import { Icon } from "#/components/atom/Icon/Icon";
import { Input } from "#/components/atom/Input/Input";
import { Label } from "#/components/atom/Label/Label";
import { H5 } from "#/components/atom/Hn/H5";
import { STATUS } from "./Filter.css";

export interface FilterProps extends HTMLAttributes<HTMLSelectElement> {
  multiselect: boolean;
  isOpen: boolean;
  children: React.ReactNode;
}

export interface OptionProps {
  children: string;
}
export function Filter({ multiselect, isOpen, children }: FilterProps) {
  const iconType = isOpen ? "up" : "down";
  const status = isOpen ? "open" : "close";
  const mainStatus = isOpen ? "openMain" : "closeMain";
  const optionStatus = isOpen ? "openOptions" : "closeOptions";
  return (
    <div className={STATUS[status]}>
      <div className={STATUS[mainStatus]}>
        <H5>뷰 선택하기</H5>
        <Icon
          type={iconType}
          color="key"
        />
      </div>
      <ul className={STATUS[optionStatus]}>{children}</ul>
    </div>
  );
}

Filter.FilterOption = function FilterOption({ children }: OptionProps) {
  return (
    <li className={STATUS["option"]}>
      <Label id="dropdown2">{children}</Label>
      <Input
        type="checkbox"
        id="dropdown"
        name="dropdown2"
        onChange={(e) => {}}
        disabled={false}
        value="hi"
      />
    </li>
  );
};
