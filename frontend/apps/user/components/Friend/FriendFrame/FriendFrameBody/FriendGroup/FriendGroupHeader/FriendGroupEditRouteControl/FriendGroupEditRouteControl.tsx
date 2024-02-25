import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { FriendGroupProps } from '../../FriendGroup';
import FRIENDS from '@U/constants/FRIENDS';
import { Btn } from '@amadda/external-temporal';
import { ROUTES } from '@U/store/friendRouter/pageSlice';

interface FriendGroupEditRouteControlProps extends FriendGroupProps {
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}

export function FriendGroupEditRouteControl({ groupName, groupSeq, groupMember }: FriendGroupProps) {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);
  switch (PATH) {
    case 'READ':
      return <FriendGroupEditRouteControl.PathIsRead groupName={groupName} groupMember={groupMember} groupSeq={groupSeq} PushToFriend={PushToFriend} />;
    case 'EDIT':
      return <FriendGroupEditRouteControl.PathIsEdit groupName={groupName} groupMember={groupMember} groupSeq={groupSeq} PushToFriend={PushToFriend} />;
    default:
      return <></>;
  }
}

FriendGroupEditRouteControl.PathIsRead = ({ PushToFriend, groupName, groupSeq, groupMember }: FriendGroupEditRouteControlProps) => {
  return (
    <Btn
      type="button"
      variant="black"
      onClick={() => {
        PushToFriend('EDIT');
        //지금 몇 번 그룹 편집?
      }}
    >
      {FRIENDS.BTN.GROUP_EDIT}
    </Btn>
  );
};
//TODO: 해당 그룹을 편집해야 하니까, 여기서 편집 상태가 된다면 전체 그룹이랑 이 그룹 멤버만
//서버 상태에서 클라이언트 상태로 만들어주고 나머지는 삭제해야 한다.

FriendGroupEditRouteControl.PathIsEdit = ({
  PushToFriend,
  groupName = FRIENDS.GROUPS.ALL_FRIENDS,
  groupSeq,
  groupMember,
}: FriendGroupEditRouteControlProps) => {
  return (
    <>
      <Btn type="button" variant="key" onClick={() => PushToFriend('READ')}>
        {FRIENDS.BTN.GROUP_SAVE}
      </Btn>
      <Btn
        type="button"
        variant="black"
        onClick={() => {
          PushToFriend('READ');
        }}
      >
        {FRIENDS.BTN.GROUP_DELETE}
      </Btn>
      <Btn type="button" variant="white" onClick={() => PushToFriend('READ')}>
        {FRIENDS.BTN.CANCEL}
      </Btn>
    </>
  );
};
