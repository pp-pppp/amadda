import React from 'react';
import type { ReactNode } from 'react';
import { CONTAINER } from './Category.css';
import { ErrorBoundary } from 'external-temporal';
import { CategoryReadResponse } from 'amadda-global-types';
import { Category } from './Category';
export interface CategoryContainerProps {
  categories: CategoryReadResponse[];
  onCategoryClick: (arg: number) => void;
}
export function CategoryContainer({ categories, onCategoryClick }: CategoryContainerProps) {
  return (
    <ErrorBoundary>
      <div className={CONTAINER}>
        {categories.map(c => (
          <Category color={c.categoryColor} categoryName={c.categoryName} key={c.categorySeq} onClick={() => onCategoryClick(c.categorySeq)} />
        ))}
      </div>
    </ErrorBoundary>
  );
}
