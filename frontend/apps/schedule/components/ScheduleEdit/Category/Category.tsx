import { CategorySelectContainer } from '@SCH/components/CategorySelect/CategorySelectContainer';
import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Spacing, Span } from '@amadda/external-temporal';

export function Category() {
  const [values, setValues] = useScheduleEditStore(state => [state.values, state.setValues]);

  return (
    <>
      <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
      <Spacing dir="v" size="1" />
      <CategorySelectContainer categories={values.category} onCategoryClick={() => setValues({ ...values, categorySeq: values.categorySeq })} />
      {/* <Spacing dir="v" size="0.5" />
                TODO: 카테고리 추가 모달 추후 구현
                <div>
                  <Input
                    type="text"
                    id="categoryadd"
                    onChange={e => setvalues.setCategoryInput(e.target.value)}
                    onKeyDown={e => {
                      e.key === 'enter' && fn.postCategory(values.categoryInput);
                      setvalues.setCategoryInput('');
                    }}
                    value={values.categoryInput}
                    name="categoryadd"
                    placeholder="카테고리 추가"
                  />
                </div> */}
      <Spacing dir="v" size="2" />
    </>
  );
}
