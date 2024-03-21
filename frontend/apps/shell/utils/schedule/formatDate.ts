export function numberToString(YYYY: number, MM: number, DD: number, TT: number, mm: number) {
  const pad = num => (num < 10 ? '0' + num : num.toString());

  const date = new Date(YYYY, MM - 1, DD, TT, mm); // 월은 0부터 시작함

  const year = date.getFullYear();
  const month = pad(date.getMonth() + 1); // getMonth()는 0부터 시작하므로 1을 더함
  const day = pad(date.getDate());
  const hour = pad(date.getHours());
  const minute = pad(date.getMinutes());

  return `${year}-${month}-${day} ${hour}:${minute}:00`;
}

export function stringToNumber(dateTimeString: string) {
  const parts = dateTimeString.match(/(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/);

  return parts?.slice(1).map(part => parseInt(part, 10)) || [];
}
