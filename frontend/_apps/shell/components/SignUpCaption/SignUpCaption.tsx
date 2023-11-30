import { COLOR } from '#/components/typography/Text/Texts.css';
import { P } from 'external-temporal';

export interface SignUpCaptionProps {
  color?: keyof typeof COLOR;
  children: string;
}

export default function SignUpCaption({ color = 'grey', children }: SignUpCaptionProps) {
  return (
    <P type="caption" color={color}>
      {children}
    </P>
  );
}
