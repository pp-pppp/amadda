import React from 'react';
import { Form, Input, Label, debounce } from '@amadda/external-temporal';
import FriendsConstants from '../../../constants/FRIENDS';
import { useForm } from '@amadda/react-util-hooks';
import { FRIEND_SEARCH_FORM_INIT } from '@U/constants/FRIEND_SEARCH_FORM_INIT';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';
import { useSearchFriend } from '@U/hooks/useFriend';

export type FriendSearch = { friendSearch: string };

export function FriendSearchInput() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);
  const [searchKey, setSearchFormData, setSearchKey] = useFriendSearchStore(state => [state.searchKey, state.setSearchFormData, state.setSearchKey]);
  const { handleChange, submit } = useForm<FriendSearch>({
    initialValues: FRIEND_SEARCH_FORM_INIT,
    onSubmit: useSearchFriend,
    setExternalStoreData: setSearchFormData,
    setExternalStoreValues: setSearchKey,
  });

  return (
    <Form formName="friendSearch" onSubmit={e => e.preventDefault()}>
      <Label htmlFor="friendSearch">
        <Input
          type="text"
          id="friendSearch"
          name="friendSearch"
          placeholder={FriendsConstants.INPUT.SEARCH}
          disabled={false}
          value={searchKey}
          onChange={e =>
            debounce(() => {
              handleChange(e);
              submit(e.target.value);
            }, 2000)
          }
          onFocus={() => PushToFriend('SEARCH')}
        />
      </Label>
    </Form>
  );
}
