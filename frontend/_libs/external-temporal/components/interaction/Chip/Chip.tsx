import React, { ReactNode } from 'react';
import { Icon } from '#/components/view/Icon/Icon';
import { TYPE_VARIANT, SHAPE_VARIANT, KEYWORD_DELETE } from './Chip.css';

interface ChipProps {
  type: keyof typeof TYPE_VARIANT;
  shape?: keyof typeof SHAPE_VARIANT;
  onFiltered?: (data: any) => void;
  onDelete?: (data: any) => void;
  children?: ReactNode;
}

export function Chip({ type, shape = 'round', onFiltered, onDelete, children, ...props }: ChipProps) {
  const classname = `${TYPE_VARIANT[type]} ${SHAPE_VARIANT[shape]}`;
  switch (type) {
    case 'filter':
      return (
        <button className={classname} onClick={onFiltered} {...props}>
          {children}
        </button>
      );
    case 'filterselected': //필터처럼 작동할 때에는 칩을 누르면 값이 상위 컴포넌트에 전달되어야 합니다. 따라서 onFlitered에 setter를 전달하면 됩니다.
      return (
        <button className={classname} onClick={onFiltered}>
          {children}
        </button>
      );
    case 'keyword':
      return (
        <span className={classname}>
          {children}
          <button className={KEYWORD_DELETE} onClick={onDelete}>
            <Icon type="close" color="white" cursor="pointer" size="20" />
          </button>
        </span>
      );
    case 'suggestion': //display only
      return (
        <span className={classname} {...props}>
          {children}
        </span>
      );
  }
}
export default Chip;
