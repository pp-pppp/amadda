import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';
import { LI_DISPLAY_VARIANT, UL_DISPLAY_VARIANT } from './list.css;
import { H3 } from '#/components/typography/hn/h3';
import Spacing from '#/components/typography/spacing/spacing';

export interface ListProps extends ComponentPropsWithoutRef<'div'> {
  title?: string;
  children: ReactNode;
}
export interface UlProps extends ComponentPropsWithoutRef<'ul'> {
  display?: keyof typeof UL_DISPLAY_VARIANT;
  children: ReactNode;
}
export interface LiProps extends ComponentPropsWithoutRef<'li'> {
  display?: keyof typeof LI_DISPLAY_VARIANT;
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

List.Ul = function ({ display = 'inline', children }: UlProps) {
  const className = UL_DISPLAY_VARIANT[display];
  return <ul className={className}>{children}</ul>;
};

List.Li = function ({ display = 'inline', children }: LiProps) {
  const className = LI_DISPLAY_VARIANT[display];
  return <li className={className}>{children}</li>;
};
