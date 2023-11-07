import Image from 'next/image';
import { FRAME } from './Profile.css';
import React from 'react';

export interface ProfileProps {
  src: string;
  alt: string;
  size?: keyof typeof FRAME;
}

const profileStyle = {
  borderRadius: '100%',
};

export function Profile({ src, alt, size = 'medium' }) {
  return (
    <div className={FRAME[size]}>
      <Image src={src} alt={alt} layout="fill" style={profileStyle} />
    </div>
  );
}
