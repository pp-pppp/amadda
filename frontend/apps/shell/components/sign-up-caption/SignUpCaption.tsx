import { P, textColors } from '@amadda/external-temporal';

export interface SignUpCaptionProps {
  color?: keyof typeof textColors;
  children: string;
}

export default function SignUpCaption({ color = 'grey', children }: SignUpCaptionProps) {
  return (
    <P type="caption" color={color}>
      {children}
    </P>
  );
}
