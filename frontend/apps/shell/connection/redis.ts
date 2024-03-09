import { createClient } from 'redis';

export const REDIS = {
  async getToken(k: string): Promise<string> {
    const client = createClient({ url: `${process.env.REDIS_ROOT as string}` });

    await client.connect();
    const token = (await client.get(k)) || '';
    await client.disconnect();
    return token;
  },
  async setToken(k: string, token: string) {
    const client = createClient({ url: `${process.env.REDIS_ROOT as string}` });
    await client.connect();
    await client.set(k, token);
    await client.disconnect();
  },
};
