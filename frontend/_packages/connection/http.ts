import { ApiResponse } from 'amadda-global-types';

export const http = {
  get: async <Res = ApiResponse<unknown>>(url: string) => {
    const result = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await result.json()) satisfies Res;
    return { status: code, message, data };
  },
  post: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await result.json()) satisfies Res;
    return { status: code, message, data };
  },
  put: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'PUT',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await result.json()) satisfies Res;
    return { status: code, message, data };
  },
  patch: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'PATCH',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await result.json()) satisfies Res;
    return { status: code, message, data };
  },
  delete: async <Res = ApiResponse<unknown>>(url: string) => {
    const result = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await result.json()) satisfies Res;
    return { status: code, message, data };
  },
};
