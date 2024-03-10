import { FriendLoading } from '@U/components/friend-group/friend-group-fallbacks/friend-loading';
import { NoFriend } from '@U/components/friend-group/friend-group-fallbacks/no-friend';
import { useFriend, useSearchFriend } from '@U/hooks/useFriend';
import { GroupHeader } from './friend-group-layout/friend-group-header';
import { Flex, H3, List } from '@amadda/external-temporal';
import FRIENDS from '@U/constants/FRIENDS';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { Friend } from '@U/components/Friend/Friend';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';

export function FriendGroupSearch() {
  const [searchKey] = useFriendSearchStore(state => [state.searchKey]);
  const { data, error, isLoading } = useSearchFriend({ friendSearch: searchKey });
  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else
    return data?.map(group => (
      <Flex key={group.groupSeq} justifyContents="flexStart">
        <GroupHeader>
          <H3>{FRIENDS.GROUPS.SEARCH_RESULT}</H3>
        </GroupHeader>
        <GroupBody>
          {group.groupMember.map(member => (
            <List.Li key={member.userSeq}>
              <Friend groupMember={member} onRequest={() => {}} />
            </List.Li>
          ))}
          )
        </GroupBody>
      </Flex>
    ));
}
