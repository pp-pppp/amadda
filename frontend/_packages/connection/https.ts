import { ApiResponse } from 'amadda-global-types';

export const https = {
  get: async <Res = ApiResponse<unknown>>(url: string, token: string) => {
    const result = await fetch(url, {
      method: 'GET',
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (!result.ok) {
      throw new Error(
        `status: ${result.status}, message: ${result.statusText}`
      );
    } else {
      const { code, message, data } = (await result.json()) satisfies Res;
      return { status: code, message, data };
    }
  },
  post: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    token: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (!result.ok) {
      throw new Error(
        `status: ${result.status}, message: ${result.statusText}`
      );
    } else {
      const { code, message, data } = (await result.json()) satisfies Res;
      return { status: code, message, data };
    }
  },
  put: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    token: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'PUT',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (!result.ok) {
      throw new Error(
        `status: ${result.status}, message: ${result.statusText}`
      );
    } else {
      const { code, message, data } = (await result.json()) satisfies Res;
      return { status: code, message, data };
    }
  },
  patch: async <Req = unknown, Res = ApiResponse<unknown>>(
    url: string,
    token: string,
    body?: Req
  ) => {
    const result = await fetch(url, {
      method: 'PATCH',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (!result.ok) {
      throw new Error(
        `status: ${result.status}, message: ${result.statusText}`
      );
    } else {
      const { code, message, data } = (await result.json()) satisfies Res;
      return { status: code, message, data };
    }
  },
  delete: async <Res = ApiResponse<unknown>>(url: string, token: string) => {
    const result = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (!result.ok) {
      throw new Error(
        `status: ${result.status}, message: ${result.statusText}`
      );
    } else {
      const { code, message, data } = (await result.json()) satisfies Res;
      return { status: code, message, data };
    }
  },
};
