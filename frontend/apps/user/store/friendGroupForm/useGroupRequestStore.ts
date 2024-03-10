import { create } from 'zustand';

import { GroupRequestFormData, groupRequestFormDataSlice } from './groupRequestFormSlice';

export const useGroupRequestFormStore = create<GroupRequestFormData>()((...a) => ({
  ...groupRequestFormDataSlice(...a),
}));
