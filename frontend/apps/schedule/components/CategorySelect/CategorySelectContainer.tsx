import React from 'react';
import { CONTAINER } from './Category.css';
import { ErrorBoundary } from '@amadda/external-temporal';
import { CategoryReadResponse } from '@amadda/global-types';
import { CategoryOption } from './CategoryOption';
export interface CategorySelectContainerProps {
  categories: CategoryReadResponse[];
  onCategoryClick: (arg: number) => void;
}
export function CategorySelectContainer({ categories, onCategoryClick }: CategorySelectContainerProps) {
  return (
    <ErrorBoundary>
      <div className={CONTAINER}>
        <>{categories?.map(category => <CategoryOption category={category} onClick={() => onCategoryClick(category.categorySeq)} />)}</>
      </div>
    </ErrorBoundary>
  );
}
