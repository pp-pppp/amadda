import React from 'react';
import { Icon } from '#/components/view/Icon/Icon';
import { TYPE_VARIANT, SHAPE_VARIANT, KEYWORD_DELETE } from './Chip.css';

interface Props {
  type: keyof typeof TYPE_VARIANT;
  label: string;
  shape?: keyof typeof SHAPE_VARIANT;
  onFlitered?: (label: string) => void;
  onDelete?: (label: string) => void;
}

export function Chip({
  type,
  label,
  shape = 'round',
  onFlitered,
  onDelete,
}: Props) {
  const classname = `${TYPE_VARIANT[type]} ${SHAPE_VARIANT[shape]}`;
  switch (type) {
    case 'filter':
      return (
        <span
          className={classname}
          onClick={() => onFlitered && onFlitered(label)}
        >
          {label}
        </span>
      );
    case 'filterselected': //필터처럼 작동할 때에는 칩을 누르면 값이 상위 컴포넌트에 전달되어야 합니다. 따라서 onFlitered에 setter를 전달하면 됩니다.
      return (
        <span
          className={classname}
          onClick={() => onFlitered && onFlitered(label)}
        >
          {label}
        </span>
      );
    case 'keyword':
      return (
        <span className={classname}>
          {label}
          <button
            className={KEYWORD_DELETE}
            onClick={() => onDelete && onDelete(label)}
          >
            <Icon type="close" color="white" cursor="pointer" />
          </button>
        </span>
      );
    case 'suggestion': //display only
      return <span className={classname}>{label}</span>;
  }
}
export default Chip;
