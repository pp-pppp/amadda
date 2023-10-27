import { memo } from 'react';
import { HSIZE, VSIZE } from './Spacing.css';

interface Props {
  dir?: 'h' | 'v';
  size: '0.5' | '1' | '2' | '3' | '5' | '10';
}

const Spacing = memo(function Spacing({ dir = 'v', size = '1' }: Props) {
  const className = `${dir === 'v' ? VSIZE[size] : HSIZE[size]}`;
  return <div className={className}></div>;
});
export default Spacing;
