import { CategorySelectContainer } from '@/components/category-select/category-select-container';
import { CREATE } from '@/constants/schedule/edit';
import { Spacing, Span } from '@amadda/external-temporal';
import { useShallow } from 'zustand/react/shallow';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';

export function Category() {
  const [values, setValues] = useScheduleEditStore(useShallow(state => [state.values, state.setValues]));

  return (
    <>
      <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
      <Spacing dir="v" size="1" />
      <CategorySelectContainer categories={values.category} onCategoryClick={() => setValues({ ...values, categorySeq: values.categorySeq })} />
      <Spacing dir="v" size="2" />
    </>
  );
}
