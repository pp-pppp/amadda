import { FriendSearch } from '@U/components/FriendFrameHeader/FriendSearchInput/FriendSearchInput';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';
import { clientFetch } from '@amadda/fetch';
import { FriendReadResponse, UserReadResponse, UserRelationResponse } from '@amadda/global-types';
import useSWR, { mutate } from 'swr';

const getFriends = () => clientFetch.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend`);

export function useFriend() {
  const { data, error, isLoading } = useSWR<FriendReadResponse>('/api/friend', getFriends);

  return {
    data,
    error,
    isLoading,
  };
}

const searchFriends = (searchKey: string) => clientFetch.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend?searchkey=${searchKey}`);
export function useSearchFriend({ friendSearch }: FriendSearch) {
  const { data, error, isLoading } = useSWR('/api/friend/searchKey', () => searchFriends(friendSearch));

  return {
    data,
    error,
    isLoading,
  };
}

const searchUser = (searchKey: string) => clientFetch.get<UserRelationResponse>(`${process.env.NEXT_PUBLIC_USER}/api/user?searchKey=${searchKey}`);
export function useSearchUser(searchKey: string) {
  const { data, error, isLoading } = useSWR('/api/user/searchKey', () => searchUser(searchKey));

  return {
    data,
    error,
    isLoading,
  };
}
