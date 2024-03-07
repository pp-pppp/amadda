import { COLOR } from './headings.css';

export interface HnProps extends ComponentPropsWithoutRef<'h1'> {
  children: ReactNode;
  color?: keyof typeof COLOR;
}
