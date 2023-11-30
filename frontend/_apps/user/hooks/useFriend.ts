import { http } from '@U/utils/http';
import { FriendReadResponse, UserReadResponse, UserRelationResponse } from 'amadda-global-types';
import useSWR, { mutate } from 'swr';

const getFriends = () => http.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend`).then(res => res.data);

export function useFriend() {
  const { data, error, isLoading } = useSWR<FriendReadResponse>('/api/friend', getFriends);

  return {
    data,
    error,
    isLoading,
  };
}

const searchFriends = (searchKey: string) =>
  http.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend?searchkey=${searchKey}`).then(res => res.data);
export function useSearchFriend(searchKey: string) {
  const { data, error, isLoading } = useSWR('/api/friend/searchKey', () => searchFriends(searchKey));

  return {
    data,
    error,
    isLoading,
  };
}

const searchUser = (searchKey: string) =>
  http.get<UserRelationResponse>(`${process.env.NEXT_PUBLIC_USER}/api/user?searchKey=${searchKey}`).then(res => res.data);
export function useSearchUser(searchKey: string) {
  const { data, error, isLoading } = useSWR('/api/user/searchKey', () => searchUser(searchKey));

  return {
    data,
    error,
    isLoading,
  };
}
