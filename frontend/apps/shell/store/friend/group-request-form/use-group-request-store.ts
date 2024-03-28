import { create } from 'zustand';

import { GroupRequestFormData, groupRequestFormDataSlice } from './group-request-form-slice';

export const useGroupRequestFormStore = create<GroupRequestFormData>()((...a) => ({
  ...groupRequestFormDataSlice(...a),
}));
