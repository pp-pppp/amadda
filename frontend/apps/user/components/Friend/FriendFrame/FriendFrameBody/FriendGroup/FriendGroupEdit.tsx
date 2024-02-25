import { Spacing } from '@amadda/external-temporal';
import { FriendGroupHeader } from './FriendGroupHeader/FriendGroupHeader';
import { FriendGroupBody } from './FriendGroupBody/FriendGroupBody';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';

export interface FriendGroupProps {
  groupSeq?: number;
  groupName?: string;
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroupEdit({ groupSeq, groupName, groupMember }: FriendGroupProps) {
  //TODO: 해당 그룹을 편집해야 하니까, 여기서 편집 상태가 된다면 전체 그룹이랑 이 그룹 멤버만
  //서버 상태에서 클라이언트 상태로 복사하고 나머지는 삭제해야 한다.
  //즉 이거는 화면을 하나 새로 파줘야한다.
  const [PATH] = useFriendRouter(state => [state.PATH]);
  return (
    <>
      <FriendGroupHeader groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
      <Spacing size="0.5" />
      <FriendGroupBody groupMember={groupMember} />
    </>
  );
}

FriendGroupEdit.Edit = ({ groupSeq, groupName, groupMember }: FriendGroupProps) => {
  return <></>;
};
