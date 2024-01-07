import { COLOR } from './H.css';

export interface HnProps extends ComponentPropsWithoutRef<'h1'> {
  children: ReactNode;
  color?: keyof typeof COLOR;
}
