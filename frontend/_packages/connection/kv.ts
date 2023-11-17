import axios from 'axios';
import { http } from './http';

export const KV = {
  async getRefreshToken(k: string): Promise<string> {
    try {
      const token = await axios
        .get(`${process.env.KV_REST_API_URL}/get/${k}`, {
          headers: {
            Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
          },
        })
        .then(res => res.data);
      return token || '';
    } catch (err) {
      console.log('redisError', err);
      return '';
    }
  },
  async setRefreshToken(k: string, token: string): Promise<void> {
    try {
      axios
        .get(`${process.env.KV_REST_API_URL}/set/${k}/${token}`, {
          headers: {
            Authorization: `Bearer ${process.env.KV_REST_API_TOKEN}`,
          },
        })
        .then(res => res);
    } catch (err) {
      console.error('redisError', err);
    }
  },
};
