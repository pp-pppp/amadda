import Error from 'next/error';

export const kv = {
  async getToken(k: string): Promise<string> {
    try {
      const token = await fetch(`${process.env.KV_REST_API_URL}/get/${k}`, {
        headers: {
          Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
        },
      }).then(res => res.json());
      return token || '';
    } catch (err: any) {
      throw new Error(err.message || 'redisError');
    }
  },
  async setToken(k: string, token: string): Promise<void> {
    try {
      fetch(`${process.env.KV_REST_API_URL}/set/${k}/${token}`, {
        headers: {
          Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
        },
      }).then(res => res);
    } catch (err: any) {
      throw new Error(err.message || 'redisError');
    }
  },
};
