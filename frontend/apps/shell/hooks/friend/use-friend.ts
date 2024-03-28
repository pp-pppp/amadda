import { FriendSearch } from '@/components/friend-search-input/friend-search-input';
import { GroupRequestForm } from '@/store/friend/group-request-form/group-request-form-slice';
import { clientFetch } from '@amadda/fetch';
import { FriendReadResponse, GroupCreateRequest, GroupUpdateRequest, User, UserRelationResponse } from '@amadda/global-types';
import useSWR from 'swr';

const getFriends = () => clientFetch.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend`);
export function useFriend() {
  const { data, error, isLoading, mutate } = useSWR<FriendReadResponse>('/api/friend', getFriends);

  return {
    data,
    error,
    isLoading,
    mutate,
  };
}

const searchFriends = (searchKey: string) => clientFetch.get<FriendReadResponse>(`${process.env.NEXT_PUBLIC_USER}/api/friend?searchkey=${searchKey}`);
export function useSearchFriend({ friendSearch }: FriendSearch) {
  const { data, error, isLoading } = useSWR(`/api/friend?${friendSearch}`, () => searchFriends(friendSearch));

  return {
    data,
    error,
    isLoading,
  };
}

const searchUser = (searchKey: string) => clientFetch.get<UserRelationResponse>(`${process.env.NEXT_PUBLIC_USER}/api/user?searchKey=${searchKey}`);
export function useSearchUser(searchKey: string) {
  const { data, error, isLoading } = useSWR(`/api/user/${searchKey}`, () => searchUser(searchKey));

  return {
    data,
    error,
    isLoading,
  };
}

export async function updateGroup(group_seq: number | null, formdata: GroupRequestForm) {
  if (!group_seq) return null;
  const userSeqs = formdata.groupMembers.map(member => member.userSeq);
  const request: GroupUpdateRequest = {
    groupName: formdata.groupName,
    userSeqs,
  };

  const data = await clientFetch.put<GroupUpdateRequest, string>(`${process.env.NEXT_PUBLIC_USER}/api/friend/${group_seq}`, request);
  return data;
}

export function createGroup(formdata: GroupRequestForm) {
  const userSeqs = formdata.groupMembers.map(member => member.userSeq);
  const request: GroupCreateRequest = {
    groupName: formdata.groupName,
    userSeqs,
  };

  const data = clientFetch.post<GroupCreateRequest, string>(`${process.env.NEXT_PUBLIC_USER}/api/friend`, request);

  return data;
}

export function deleteFriend(friend: User) {
  const friend_user_seq = friend.userSeq;

  const data = clientFetch.delete<string>(`${process.env.NEXT_PUBLIC_USER}/api/friend/${friend_user_seq}`);
  return data;
}
