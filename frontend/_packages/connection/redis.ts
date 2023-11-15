import { createClient } from 'redis';
const client = createClient({
  url: `redis://default:${process.env.REDIS_PASS}@${process.env.REDIS_ROOT}`,
});

client.on('error', err => console.log('Redis Client Error', err));
client.connect();

export const REDIS = {
  async getRefreshToken(k: string): Promise<string> {
    try {
      const token = await client.get(k);
      return token || '';
    } catch (err) {
      console.log('redisError', err);
      return '';
    }
  },
  async setRefreshToken(k: string, token: string): Promise<void> {
    try {
      await client.set(k, token);
    } catch (err) {
      console.error('redisError', err);
    }
  },
};
