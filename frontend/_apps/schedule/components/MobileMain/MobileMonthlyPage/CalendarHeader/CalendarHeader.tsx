import { useDateStore } from '@SCH/store/dateStore';
import { BtnRound, Filter, Flex, H2, P, Spacing } from 'external-temporal';
import { useEffect, useState, MouseEvent, ChangeEvent } from 'react';
import CALENDAR from '@SCH/constants/CALENDAR';
import useCategory from '@SCH/hooks/useCategory';
import { useCategoryStore } from '@SCH/store/categoryStore';
import { useFilterStore } from '@SCH/store/filterStore';
import useServerTime from '@SCH/hooks/useServerTime';

export function CalendarHeader() {
  const { data } = useServerTime();
  const { isOpen } = useFilterStore();
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const { selectedCategorySeq, selectedAll, selectedNone } = useCategoryStore();
  const { categories, setCategories } = useCategory();

  useEffect(() => {
    if (selectedCategorySeq.length === categories.length && selectedNone) {
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
        categories.length !== 0 &&
          categories.map((category, idx) => {
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
          {categories.map((category, idx) => {
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
