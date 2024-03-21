import { GroupRequestForm } from '@U/store/group-request-form/group-request-form-slice';

export function friendGroupValidator(formdata: GroupRequestForm) {
  if (formdata.groupName.length === 0) return false;
  if (formdata.groupMembers.length === 0) return false;
  else return true;
}
