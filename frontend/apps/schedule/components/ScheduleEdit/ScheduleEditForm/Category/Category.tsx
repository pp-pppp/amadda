import { CategorySelectContainer } from '@SCH/components/CategorySelect/CategorySelectContainer';
import { CREATE } from '@SCH/constants/CREATE';
import { Spacing, Span } from '@amadda/external-temporal';
import { useContext } from 'react';
import { ScheduleFormContext } from '../ScheduleEditForm';

export function Category() {
  const { values, setValues } = useContext(ScheduleFormContext);

  return (
    <>
      <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
      <Spacing dir="v" size="1" />
      <CategorySelectContainer categories={values.category} onCategoryClick={() => setValues({ ...values, categorySeq: values.categorySeq })} />
      <Spacing dir="v" size="2" />
    </>
  );
}
