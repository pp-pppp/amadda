import { KAFKA_NOTICE } from '../../kafka/kafka';
import { auth } from '@amadda/fetch';
import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

async function handler(req: NextApiRequest, res: NextApiResponse) {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  const consumer = KAFKA_NOTICE;
  try {
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        const payload = message.value && message.value.toString();
        res.write(`${payload}`);
      },
    });
    req.on('close', () => {
      return res.end();
    });
  } catch (err) {
    Sentry.captureException(err);
    return res.status(500).json({ data: 'internal server error' });
  }
}
export default Sentry.wrapApiHandlerWithSentry(auth(handler), 'notice/api/event');
