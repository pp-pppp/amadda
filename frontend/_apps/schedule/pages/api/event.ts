import { KAFKA_SCHEDULE } from 'amadda-kafka';
import { auth } from 'connection';
import { wrapApiHandlerWithSentry } from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

async function handler(req: NextApiRequest, res: NextApiResponse) {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');

  try {
    const consumer = await KAFKA_SCHEDULE();

    consumer &&
      (await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
          res.status(204);
        },
      }));

    req.on('close', () => {
      console.log('Client disconnected');
      return res.end();
    });
  } catch (err) {
    return res.status(500).json({ data: 'internal server error' });
  }
}
export default wrapApiHandlerWithSentry(auth(handler), 'schedule/api/event');
