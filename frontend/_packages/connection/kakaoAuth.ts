import { ApiResponse } from 'amadda-global-types';

export default {
  get: async (url: string) => {
    const result = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (result.status > 399) throw new Error(result.statusText || 'kakaoError');
    const response = await result.json();
    return response;
  },
  post: async (url: string, body?: any) => {
    const result = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (result.status > 399) throw new Error(result.statusText || 'kakaoError');
    const response = await result.json();
    return response;
  },
  secureGet: async (url: string, token: string) => {
    const result = await fetch(url, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (result.status > 399) throw new Error(result.statusText || 'kakaoError');
    const response = await result.json();
    return response;
  },
  securepost: async (url: string, token: string, body?: any) => {
    const result = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (result.status > 399) throw new Error(result.statusText || 'kakaoError');
    const response = await result.json();
    return response;
  },
  secureDelete: async (url: string, token: string) => {
    const result = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (result.status > 399) throw new Error(result.statusText || 'kakaoError');
    const response = await result.json();
    return response;
  },
};
