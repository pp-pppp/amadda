import { CategorySelectContainer } from '@SCH/components/CategorySelect/CategorySelectContainer';
import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { useShallow } from 'zustand/react/shallow';
import { Spacing, Span } from '@amadda/external-temporal';

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
