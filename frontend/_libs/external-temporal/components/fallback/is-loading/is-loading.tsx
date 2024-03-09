import React from 'react';
import { BASE } from './is-loading.css';
import { H5 } from '#/components/typography/hn/h5';

export function Loading() {
  return (
    <div className={BASE}>
      <H5>정보를 가져오고 있어요</H5>
    </div>
  );
}
