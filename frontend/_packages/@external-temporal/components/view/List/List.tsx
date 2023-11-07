import React from 'react';
import type { HTMLAttributes, ReactNode } from 'react';
import { LI_BASE, UL_BASE } from './List.css';
import { H3 } from '#/components/typography/Hn/H3';
import Spacing from '#/components/typography/Spacing/Spacing';

interface ListProps extends HTMLAttributes<HTMLDivElement> {
  title?: string;
  children: ReactNode;
}
interface UlProps extends HTMLAttributes<HTMLUListElement> {
  children: ReactNode;
}
interface LiProps extends HTMLAttributes<HTMLLIElement> {
  children: ReactNode;
}

export function List({ title = '', children }: ListProps) {
  return (
    <div>
      {title.length > 0 && (
        <>
          <H3>{title}</H3>
          <Spacing />
        </>
      )}
      {children}
    </div>
  );
}

List.Ul = function ({ children }: UlProps) {
  return <ul className={UL_BASE}>{children}</ul>;
};

List.Li = function ({ children }: LiProps) {
  return <li className={LI_BASE}>{children}</li>;
};
