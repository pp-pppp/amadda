import { FriendSearch } from '@U/components/Friend/FriendFrame/FriendFrameHeader/FriendSearchInput/FriendSearchInput';
import { UseForm } from '@amadda/react-util-hooks';
import { ChangeEvent } from 'react';
import type { StateCreator } from 'zustand';

export interface FriendSearchInput {
  searchKey: string;
  setSearchKey: ({ friendSearch }: FriendSearch) => void;
  searchFormData: UseForm<FriendSearch>;
  setSearchFormData: (data: UseForm<FriendSearch>) => void;
}
export const friendSearchInputSlice: StateCreator<FriendSearchInput, [], [], FriendSearchInput> = set => ({
  searchKey: '',
  setSearchKey: ({ friendSearch }) => set(state => ({ searchKey: friendSearch })),
  searchFormData: {
    values: {
      friendSearch: '',
    },
    setValues: undefined,
    refValues: null,
    handleChange: function (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>): Promise<void> {
      throw new Error('Function not implemented.');
    },
    invalidFields: [],
    refs: null,
    submit: function (data: any): Promise<unknown> {
      throw new Error('Function not implemented.');
    },
    isLoading: false,
    response: undefined,
  },
  setSearchFormData: data => set(state => ({ searchFormData: data })),
});
