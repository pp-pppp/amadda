import React from 'react';
import {
  ALIGN_VARIANT,
  DIRECTION_VARIANT,
  JUSTIFY_VARIANT,
  WRAP_VARIANT,
} from './Flex.css';
export interface FlexProps {
  flexDirection?: keyof typeof DIRECTION_VARIANT;
  justifyContents: keyof typeof JUSTIFY_VARIANT;
  alignItems?: keyof typeof ALIGN_VARIANT;
  flexWrap?: keyof typeof WRAP_VARIANT;
  children: React.ReactNode;
}

export function Flex({
  flexDirection = 'row',
  justifyContents,
  alignItems = 'center',
  flexWrap = 'wrap',
  children,
}: FlexProps) {
  const className = `${DIRECTION_VARIANT[flexDirection]} ${JUSTIFY_VARIANT[justifyContents]} ${ALIGN_VARIANT[alignItems]} ${WRAP_VARIANT[flexWrap]}`;
  return <div className={className}>{children}</div>;
}

export default Flex;
