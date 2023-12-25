import { ApiResponse } from 'amadda-global-types';

export const https = {
  get: async <Res = ApiResponse<unknown>>(url: string, token: string) => {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await response.json()) satisfies Res;

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return { status: code, message, data };
    }
  },
  post: async <Req = unknown, Res = ApiResponse<unknown>>(url: string, token: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await response.json()) satisfies Res;

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return { status: code, message, data };
    }
  },
  put: async <Req = unknown, Res = ApiResponse<unknown>>(url: string, token: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'PUT',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await response.json()) satisfies Res;

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return { status: code, message, data };
    }
  },
  patch: async <Req = unknown, Res = ApiResponse<unknown>>(url: string, token: string, body?: Req) => {
    const response = await fetch(url, {
      method: 'PATCH',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await response.json()) satisfies Res;

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return { status: code, message, data };
    }
  },
  delete: async <Res = ApiResponse<unknown>>(url: string, token: string) => {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `${token}`,
        'Content-Type': 'application/json',
      },
    });
    const { code, message, data } = (await response.json()) satisfies Res;

    if (response.status > 399) {
      throw new Error(`status: ${code || response.status}, message: ${message || response.statusText}`);
    } else {
      return { status: code, message, data };
    }
  },
};
