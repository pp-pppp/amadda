import { KAFKA_NOTICE } from 'amadda-kafka';
import { auth } from 'connection';
import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

async function handler(req: NextApiRequest, res: NextApiResponse) {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');

  try {
    const consumer = await KAFKA_NOTICE();

    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        const payload = message.value && message.value.toString();
        res.write(`data: ${payload}\n\n`);
      },
    });

    req.on('close', () => {
      console.log('Client disconnected');
      return res.end();
    });
  } catch (err) {
    return res.status(500).json({ data: 'internal server error' });
  }
}
export default wrapApiHandlerWithSentry(auth(handler), 'notice/api/event');
