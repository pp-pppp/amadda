import React from 'react';
import { Form, Input, Label, Spacing } from '@amadda/external-temporal';
import FriendsConstants from '../../../../../constants/FRIENDS';
import { useState } from 'react';
import { useSearchFriend, useSearchUser } from '@U/hooks/useFriend';
import { useForm } from '@amadda/react-util-hooks';
import { FRIEND_SEARCH_FORM_INIT } from '@U/constants/FRIEND_SEARCH_FORM_INIT';

type friendSearch = { friendSearch: string };

export function FriendSearchInput() {
  const { values, handleChange } = useForm<friendSearch>({
    initialValues: FRIEND_SEARCH_FORM_INIT,
    onSubmit: () => new Promise(() => {}),
  });

  return (
    <>
      <Form
        formName="friendSearch"
        onSubmit={e => {
          e.preventDefault();
        }}
      >
        <Label htmlFor="friendSearch">
          <Input
            type="text"
            id="friendSearch"
            name="friendSearch"
            disabled={false}
            value={values.friendSearch}
            onChange={handleChange}
            placeholder={FriendsConstants.INPUT.SEARCH}
          />
        </Label>
      </Form>
    </>
  );
}
