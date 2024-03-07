import { FriendGroups } from '@/components/friend-groups/friend-groups';
import { Friend } from '@/components/friend-component/friend';
import { useFriend } from '@/hooks/friend/useFriend';
import { ErrorBoundary } from '@amadda/external-temporal';
import { clientFetch } from '@amadda/fetch';
import FRIENDS from '@/constants/friend/FRIENDS';

export default function FriendPage() {
  const { data, isLoading, error } = useFriend();
  return (
    <div>
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
                      onQuit={async () => await clientFetch.delete(`${process.env.NEXT_PUBLIC_USER}/friend/${f.userSeq}`)}
                    />
                  ))}
                </FriendGroups>
              );
          })}
      </ErrorBoundary>
    </div>
  );
}
