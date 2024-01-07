import React from 'react';
import { COLOR_VARIANTS } from './Category.css';
export interface CategoryProps {
  color: keyof typeof COLOR_VARIANTS;
  categoryName: string;
  onClick?: (data: any) => void;
}
export function Category({ color, categoryName, onClick }: CategoryProps) {
  return (
    <div onClick={onClick}>
      <span className={COLOR_VARIANTS[color]}>{categoryName}</span>
    </div>
  );
}
