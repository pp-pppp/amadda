import { KAFKA_SCHEDULE } from '@amadda/kafka';
import { auth } from '@amadda/fetch';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

async function handler(req: NextApiRequest, res: NextApiResponse) {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  const consumer = await KAFKA_SCHEDULE();
  try {
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        res.status(204);
      },
    });

    req.on('close', () => {
      console.log('Client disconnected');
      consumer.disconnect();
      return res.end();
    });
  } catch (err) {
    Sentry.captureException(err);
    await consumer.disconnect();
    return res.status(500).json({ data: 'internal server error' });
  }
}
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'schedule/api/event');
