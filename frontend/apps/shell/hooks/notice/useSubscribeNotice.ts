import useSWRSubscription from 'swr/subscription';

export function useSubscribeNotice() {
  const { data, error } = useSWRSubscription('/notice/event', (key, { next }) => {
    const eventSource = new EventSource(`${process.env.NEXT_PUBLIC_NOTICE}/api/eventsrc`);
    eventSource.onmessage = async (e: MessageEvent) => next(null, e.data);
    eventSource.onerror = async (error: Event) => next(error);
    return () => eventSource.close();
  });

  return { data, error };
}
