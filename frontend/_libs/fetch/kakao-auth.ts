export const kakaoAuth = {
  get: async (url: string) => {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (response.status > 399) throw new Error(response.statusText || 'kakaoError');
    const data = await response.json();
    return data;
  },
  post: async (url: string, body?: any) => {
    const response = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (response.status > 399) throw new Error(response.statusText || 'kakaoError');
    const data = await response.json();
    return data;
  },
  secureGet: async (url: string, token: string) => {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (response.status > 399) throw new Error(response.statusText || 'kakaoError');
    const data = await response.json();
    return data;
  },
  securepost: async (url: string, token: string, body?: any) => {
    const response = await fetch(url, {
      method: 'POST',
      body: JSON.stringify(body) || null,
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (response.status > 399) throw new Error(response.statusText || 'kakaoError');
    const data = await response.json();
    return data;
  },
  secureDelete: async (url: string, token: string) => {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
      },
    });
    if (response.status > 399) throw new Error(response.statusText || 'kakaoError');
    const data = await response.json();
    return data;
  },
};
