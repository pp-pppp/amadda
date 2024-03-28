import React, { ChangeEvent } from 'react';
import { Form, Input, Label, debounce } from '@amadda/external-temporal';
import FriendsConstants from '../../constants/friend/friend-ui';
import { useForm } from '@amadda/react-util-hooks';
import { FRIEND_SEARCH_FORM_INIT } from '@/constants/friend/friend-search-form-init';
import { useFriendRouter } from '@/store/friend/friend-router/use-friend-router';
import { useFriendSearchStore } from '@/store/friend/friend-search-form/use-friend-search-store';
import { useSearchFriend } from '@/hooks/friend/use-friend';

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

  const autoComplete = (e: ChangeEvent<HTMLInputElement & HTMLTextAreaElement>) =>
    debounce(() => {
      handleChange(e);
      submit(e.target.value);
    }, 2000);

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
          onChange={autoComplete}
          onFocus={() => PushToFriend('SEARCH')}
        />
      </Label>
    </Form>
  );
}
