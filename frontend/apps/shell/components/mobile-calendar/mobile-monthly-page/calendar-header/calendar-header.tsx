import { useDateStore } from '@/store/schedule/date-store';
import { BtnRound, Filter, Flex, H2, P, Spacing } from '@amadda/external-temporal';
import { useEffect, useState, MouseEvent, ChangeEvent } from 'react';
import CALENDAR from '@/constants/schedule/calendar';
import { useGetCategory } from '@/hooks/schedule/use-category';
import { useCategoryStore } from '@/store/schedule/category-store';
import { useFilterStore } from '@/store/schedule/filter-store';
import useServerTime from '@/hooks/schedule/use-server-time';

export function CalendarHeader() {
  const { data } = useServerTime();
  const { isOpen } = useFilterStore();
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const { selectedCategorySeq, selectedAll, selectedNone } = useCategoryStore();
  const { category } = useGetCategory();

  useEffect(() => {
    if (selectedCategorySeq.length === category.length && selectedNone) {
      useCategoryStore.setState(() => ({
        selectedAll: true,
      }));
    }
  }, [selectedCategorySeq, selectedNone]);

  const filterOpen = (e: MouseEvent) => {
    e.stopPropagation();
    useFilterStore.setState(prevState => ({
      isOpen: !prevState.isOpen,
    }));
  };

  const selectAll = () => {
    useCategoryStore.setState(prevState => {
      if (!prevState.selectedAll) {
        const categorySeqs: number[] = [];
        category.length !== 0 &&
          category.map((category, idx) => {
            categorySeqs.push(category.categorySeq);
          });

        return {
          selectedCategorySeq: categorySeqs,
          selectedAll: true,
          selectedNone: true,
        };
      } else {
        return {
          selectedCategorySeq: [],
          selectedAll: false,
          selectedNone: false,
        };
      }
    });
  };

  const selectNone = () => {
    useCategoryStore.setState(prevState => {
      if (prevState.selectedNone) {
        return {
          selectedCategorySeq: prevState.selectedCategorySeq,
          selectedAll: false,
          selectedNone: false,
        };
      } else {
        return {
          selectedCategorySeq: prevState.selectedCategorySeq,
          selectedAll: false,
          selectedNone: true,
        };
      }
    });
  };

  const selectCategory = (categorySeq: number) => {
    useCategoryStore.setState(prevState => {
      if (prevState.selectedCategorySeq.includes(categorySeq)) {
        return {
          selectedCategorySeq: prevState.selectedCategorySeq.filter(seq => seq !== categorySeq),
          selectedAll: false,
          selectedNone: prevState.selectedNone,
        };
      } else {
        return {
          selectedCategorySeq: [...prevState.selectedCategorySeq, categorySeq],
        };
      }
    });
  };

  const goToday = () => {
    useDateStore.setState(state => ({
      ...state,
      selectedYear: data.split('-')[0],
      selectedMonth: data.split('-')[1],
      selectedDate: data.split('-')[2],
    }));
  };

  return (
    <>
      <Flex justifyContents="spaceBetween" alignItems="center">
        <P color="grey">{selectedYear}</P>
        <BtnRound type="button" variant="key" disabled={false} onClick={() => goToday()}>
          {CALENDAR.TODAY}
        </BtnRound>
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="spaceBetween">
        <H2>{selectedMonth}월</H2>
        <Filter isOpen={isOpen} onClick={filterOpen} main="카테고리">
          <Filter.FilterOption
            id="category_all"
            name="category_all"
            onChange={() => selectAll()}
            value={CALENDAR.CATEGORY_ALL}
            checked={selectedAll ? true : false}
          >
            {CALENDAR.CATEGORY_ALL}
          </Filter.FilterOption>
          {category.map((category, idx) => {
            return (
              <Filter.FilterOption
                key={idx}
                id={'category_' + category.categorySeq}
                name={'category_' + category.categorySeq}
                onChange={() => selectCategory(category.categorySeq)}
                value={category.categoryName}
                checked={selectedAll || selectedCategorySeq.includes(category.categorySeq)}
              >
                {category.categoryName}
              </Filter.FilterOption>
            );
          })}
          <Filter.FilterOption
            id="category_none"
            name="category_none"
            onChange={e => selectNone()}
            value={CALENDAR.CATEGORY_NONE}
            checked={selectedNone ? true : false}
          >
            {CALENDAR.CATEGORY_NONE}
          </Filter.FilterOption>
        </Filter>
      </Flex>
    </>
  );
}
