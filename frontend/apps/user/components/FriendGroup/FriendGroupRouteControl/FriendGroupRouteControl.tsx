import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { FriendGroupProps } from '../FriendGroup';
import FRIENDS from '@U/constants/FRIENDS';
import { Btn } from '@amadda/external-temporal';
import { ROUTES } from '@U/store/friendRouter/pageSlice';

interface FriendGroupRouteControlProps {
  groupSeq: number | undefined;
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
  setEditing: (to: number | null) => void;
}

export function FriendGroupRouteControl({ groupName, groupSeq, groupMember }: FriendGroupProps) {
  const [PATH, PushToFriend, setEditing] = useFriendRouter(state => [state.PATH, state.PushToFriend, state.setEditing]);
  switch (PATH) {
    case 'READ':
      return <FriendGroupRouteControl.PathIsRead groupSeq={groupSeq} setEditing={setEditing} PushToFriend={PushToFriend} />;
    case 'EDIT':
      return <FriendGroupRouteControl.PathIsEdit groupSeq={groupSeq} setEditing={setEditing} PushToFriend={PushToFriend} />;
    default:
      return <></>;
  }
}

FriendGroupRouteControl.PathIsRead = ({ groupSeq, setEditing, PushToFriend }: FriendGroupRouteControlProps) => {
  return (
    <Btn
      type="button"
      variant="black"
      onClick={() => {
        if (!groupSeq) PushToFriend('READ');
        else {
          PushToFriend('EDIT');
          setEditing(groupSeq);
        }
      }}
    >
      {FRIENDS.BTN.GROUP_EDIT}
    </Btn>
  );
};
//TODO: 해당 그룹을 편집해야 하니까, 여기서 편집 상태가 된다면 전체 그룹이랑 이 그룹 멤버만
//서버 상태에서 클라이언트 상태로 만들어주고 나머지는 삭제해야 한다.

FriendGroupRouteControl.PathIsEdit = ({ groupSeq, setEditing, PushToFriend }: FriendGroupRouteControlProps) => {
  return (
    <>
      <Btn
        type="submit"
        variant="key"
        onClick={() => {
          PushToFriend('READ');
          setEditing(null);
        }}
      >
        {FRIENDS.BTN.GROUP_SAVE}
      </Btn>
      <Btn
        type="submit"
        variant="black"
        onClick={() => {
          PushToFriend('READ');
          setEditing(null);
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
