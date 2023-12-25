import { createClient } from 'redis';

export const REDIS = {
  async getToken(k: string): Promise<string> {
    const client = createClient({ url: `${process.env.REDIS_ROOT as string}` });
    /*
		{
    username: 'default', // use your Redis user. More info https://redis.io/docs/management/security/acl/
    password: 'secret', // use your password here
    socket: {
        host: 'my-redis.cloud.redislabs.com',
        port: 6379,
        tls: true,
        key: readFileSync('./redis_user_private.key'),
        cert: readFileSync('./redis_user.crt'),
        ca: [readFileSync('./redis_ca.pem')]
    }
}
		*/
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
export const KAFKA = {};
