export const KV = {
  async getRefreshToken(k: string): Promise<string> {
    try {
      const token = await fetch(`${process.env.KV_REST_API_URL}/get/${k}`, {
        headers: {
          Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
        },
      }).then(res => res.json());
      return token || '';
    } catch (err) {
      console.log('redisError', err);
      return '';
    }
  },
  async setRefreshToken(k: string, token: string): Promise<void> {
    try {
      fetch(`${process.env.KV_REST_API_URL}/set/${k}/${token}`, {
        headers: {
          Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
        },
      }).then(res => res);
    } catch (err) {
      console.error('redisError', err);
    }
  },
};
