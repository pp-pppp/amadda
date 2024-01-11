import { ChangeEvent, FocusEvent, KeyboardEvent } from 'react';

export interface InputBaseProps extends ComponentPropsWithoutRef<'input'> {
  type: 'text' | 'checkbox' | 'number';
  validator?: (target: string | number) => boolean;
  id: string;
  name: string;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
  onKeyDown?: (e: KeyboardEventt) => void;
  onFocus?: (e: FocusEvent) => void;
  disabled?: boolean;
  placeholder?: string;
  value?: string | number;
  checked?: boolean;
  autoComplete?: undefined | 'off';
}
