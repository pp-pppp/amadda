import { ApiResponse, UserReadResponse } from 'amadda-global-types';

/**
 * `clientFetch`는 `next.js` 클라이언트 사이드에서 단독으로 사용하거나,
 * `SWR`의 `fetcher` 메서드로 사용할 수 있습니다.
 * 서버 간 통신에는 `http`, `https`를 사용해 주세요.
 *
 * @returns `get(), post(), put(), patch(), delete() methods`
 * @example
 * ```
 * export function setUserData(id, data, options) {
 *  const { data, error, isLoading, isValidating, mutate } = useSWR(url, () => clientFetch.post(url, data), options);
 *  return { data, isError: error, isLoading };
 * }
 * ```
 */
export const clientFetch = {
  /**
   * @param url - next.app의 api endpoint
   * @returns 상태 코드, 에러 메세지가 있다면 에러 메세지, 응답 데이터가 있다면 데이터를 리턴합니다.
   *
   */
  get: async <Res = unknown>(url: string) => {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data }: ApiResponse<Res> = await response.json();

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return data;
    }
  },
  /**
   * @param url - next.app의 api endpoint
   * @param body - 서버로 제출할 데이터
   * @returns 상태 코드, 에러 메세지가 있다면 에러 메세지, 응답 데이터가 있다면 데이터
   *
   */
  post: async <Req = unknown, Res = unknown>(url: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data }: ApiResponse<Res> = await response.json();

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return data;
    }
  },
  /**
   * @param url - next.app의 api endpoint
   * @param body - 서버로 제출할 데이터
   * @returns 상태 코드, 에러 메세지가 있다면 에러 메세지, 응답 데이터가 있다면 데이터
   *
   */
  put: async <Req = unknown, Res = unknown>(url: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'PUT',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data }: ApiResponse<Res> = await response.json();

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return data;
    }
  },
  /**
   * @param url - next.app의 api endpoint
   * @param body - 서버로 제출할 데이터
   * @returns 상태 코드, 에러 메세지가 있다면 에러 메세지, 응답 데이터가 있다면 데이터
   *
   */
  patch: async <Req = unknown, Res = unknown>(url: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'PATCH',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data }: ApiResponse<Res> = await response.json();

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return data;
    }
  },
  /**
   * @param url - next.app의 api endpoint
   * @returns 상태 코드, 에러 메세지가 있다면 에러 메세지, 응답 데이터가 있다면 데이터
   *
   */
  delete: async <Res = unknown>(url: string) => {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data }: ApiResponse<Res> = await response.json();

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return data;
    }
  },
};
