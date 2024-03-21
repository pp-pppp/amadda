import React from 'react';
import { COLOR_VARIANTS } from './category-select.css';
export interface CategoryProps {
  category: {
    categoryColor: keyof typeof COLOR_VARIANTS;
    categoryName: string;
  };
  onClick?: (data: any) => void;
}
export function CategoryOption({ category: { categoryColor, categoryName }, onClick }: CategoryProps) {
  return (
    <div onClick={onClick}>
      <span className={COLOR_VARIANTS[categoryColor]}>{categoryName}</span>
    </div>
  );
}
