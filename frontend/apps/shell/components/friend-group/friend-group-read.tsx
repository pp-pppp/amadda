import { FriendLoading } from '@/components/friend-group/friend-group-fallbacks/friend-loading';
import { NoFriend } from '@/components/friend-group/friend-group-fallbacks/no-friend';
import { deleteFriend, useFriend } from '@/hooks/friend/use-friend';
import { GroupHeader } from './friend-group-layout/friend-group-header';
import { BtnRound, Flex, H3, List } from '@amadda/external-temporal';
import FRIENDS from '@/constants/friend/friend-ui';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { Friend } from '@/components/friend-component/friend';
import { useFriendRouter } from '@/store/friend/friend-router/use-friend-router';

export function FriendGroupRead() {
  const [pushToFriend, setEditing] = useFriendRouter(state => [state.PushToFriend, state.setUpdatingGroupSeq]);

  const { data, isLoading, error, mutate } = useFriend();
  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else
    return data?.map(group => (
      <Flex key={group.groupSeq} justifyContents="flexStart">
        <GroupHeader>
          <H3>{group.groupName || FRIENDS.GROUPS.ALL_FRIENDS}</H3>
          {group.groupSeq && (
            <BtnRound
              type="button"
              variant="key"
              onClick={() => {
                setEditing(group.groupSeq);
                pushToFriend('UPDATE');
              }}
            >
              {FRIENDS.BTN.GROUP_UPDATE}
            </BtnRound>
          )}
        </GroupHeader>
        <GroupBody>
          {group.groupMember.map(member => (
            <List.Li key={member.userSeq}>
              <Friend
                groupMember={member}
                onQuit={async friend => {
                  await deleteFriend(friend);
                  mutate();
                }}
              />
            </List.Li>
          ))}
          )
        </GroupBody>
      </Flex>
    ));
}
