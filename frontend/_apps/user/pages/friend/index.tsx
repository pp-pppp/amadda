import { FriendFrame } from '@U/components/Friend/FriendFrame/FriendFrame';
import { FriendGroups } from '@U/components/Friend/FriendGroups/FriendGroups';
import { Friend } from '@U/components/Friend/Friend/Friend';
import FRIENDS from '@U/constants/FRIENDS';
import { useFriend } from '@U/hooks/useFriend';
import { http } from '@U/utils/http';
import { ErrorBoundary } from 'external-temporal';

export default function FriendPage() {
  const { data, isLoading, error } = useFriend();
  return (
    <div>
      <FriendFrame>
        <ErrorBoundary>
          {data &&
            data.map(g => {
              if (g.groupSeq !== null) {
                return (
                  <FriendGroups key={g.groupSeq} groupSeq={g.groupSeq} groupName={g.groupName}>
                    {g.groupMember.map(f => (
                      <Friend key={f.userId} {...f} />
                    ))}
                  </FriendGroups>
                );
              } else
                return (
                  <FriendGroups key={g.groupSeq} groupSeq={g.groupSeq} groupName={FRIENDS.GROUPS.ALL_FRIENDS}>
                    {g.groupMember.map(f => (
                      <Friend
                        key={f.userId}
                        {...f}
                        status="quit"
                        onQuit={async () => {
                          const res = await http
                            .delete(`${process.env.NEXT_PUBLIC_USER}/friend/${f.userSeq}`)
                            .then(res => res.data)
                            .catch(err => '');
                        }}
                      />
                    ))}
                  </FriendGroups>
                );
            })}
        </ErrorBoundary>
      </FriendFrame>
    </div>
  );
}
