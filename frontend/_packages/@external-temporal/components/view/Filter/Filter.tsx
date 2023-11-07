import React from 'react';
import { Icon } from '#/components/view/Icon/Icon';
import { Input } from '#/components/interaction/Input/Input';
import { Label } from '#/components/interaction/Label/Label';
import { H5 } from '#/components/typography/Hn/H5';
import { STATUS } from './Filter.css';

export interface FilterProps {
  isOpen: boolean;
  onClick?(e: React.MouseEvent): void;
  main: string;
  children: React.ReactNode;
}

export interface OptionProps {
  id: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  value: string;
  children: string;
}

export function Filter({ isOpen, onClick, main, children }: FilterProps) {
  const iconType = isOpen ? 'up' : 'down';
  const status = isOpen ? 'open' : 'close';
  const mainStatus = isOpen ? 'openMain' : 'closeMain';
  const optionStatus = isOpen ? 'openOptions' : 'closeOptions';
  return (
    <div className={STATUS[status]}>
      <div className={STATUS[mainStatus]} onClick={onClick}>
        <H5>{main}</H5>
        <Icon type={iconType} color="key" />
      </div>
      <ul className={STATUS[optionStatus]}>{children}</ul>
    </div>
  );
}

Filter.FilterOption = function FilterOption({
  id,
  name,
  onChange,
  value,
  children,
}: OptionProps) {
  return (
    <li className={STATUS['option']}>
      <Label htmlFor={id}>{children}</Label>
      <Input
        type="checkbox"
        id={id}
        name={name}
        onChange={onChange}
        disabled={false}
        value={value}
      />
    </li>
  );
};