import React from 'react';
import { createContext, useContext, useState } from 'react';
import { CONTENTS, SEGMENT_CONTAINER, TRIGGER_CONTAINER, TRIGGER_VARIANT } from './segment-block.css';
import Spacing from '#/components/typography/spacing/spacing';
import type { ReactNode } from 'react';

export interface SegmentProps {
  titles: string[];
  pages: ReactNode[];
}
export interface SegmentTriggerProps {
  titles: string[];
}
export interface SegmentContentProps {
  pages: ReactNode[];
}

const SegmentContext = createContext<{
  page: number;
  setPage: (...args: number[]) => void;
}>({ page: 0, setPage: (...args) => {} });

export function Segments({ titles, pages }: SegmentProps) {
  const [curPage, setCurPage] = useState<number>(0);
  return (
    <SegmentContext.Provider value={{ page: curPage, setPage: setCurPage }}>
      <div className={SEGMENT_CONTAINER}>
        <Segments.Triggers titles={titles} />
        <Spacing size="2" />
        <Segments.Contents pages={pages} />
      </div>
    </SegmentContext.Provider>
  );
}

Segments.Triggers = function ({ titles }: SegmentTriggerProps) {
  const { page, setPage } = useContext(SegmentContext);
  return (
    <div className={TRIGGER_CONTAINER}>
      {titles.map((title: string, idx: number) => (
        <button key={idx} className={TRIGGER_VARIANT[page === idx ? 'selected' : 'unselected']} onClick={() => setPage(idx)}>
          {titles[idx]}
        </button>
      ))}
    </div>
  );
};

Segments.Contents = function ({ pages }: SegmentContentProps) {
  const { page, setPage } = useContext(SegmentContext);
  return <div className={CONTENTS}>{pages[page]}</div>;
};
