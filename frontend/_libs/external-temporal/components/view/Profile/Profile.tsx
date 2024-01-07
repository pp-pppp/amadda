import React from 'react';
import Image from 'next/image';
import { FRAME } from './Profile.css';

export interface ProfileProps {
  src: string;
  alt: string;
  size?: keyof typeof FRAME;
}

const profileStyle = {
  borderRadius: '100%',
};

export function Profile({ src, alt, size = 'medium' }: ProfileProps) {
  return <div className={FRAME[size]}>{src && <Image src={src} alt={alt} layout="fill" style={profileStyle} />}</div>;
}
