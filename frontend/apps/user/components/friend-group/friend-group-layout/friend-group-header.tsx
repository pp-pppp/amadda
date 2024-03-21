import { Flex, H3 } from '@amadda/external-temporal';
import { ReactNode } from 'react';

export function GroupHeader({ children, ...props }: { children: ReactNode }) {
  return (
    <Flex flexDirection="row" justifyContents="flexStart" alignItems="center" {...props}>
      {children}
    </Flex>
  );
}
