import { KAFKA_SCHEDULE } from 'amadda-kafka';
import { NextApiRequest, NextApiResponse } from 'next';

export default async function event(req: NextApiRequest, res: NextApiResponse) {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');

  try {
    const consumer = await KAFKA_SCHEDULE();

    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        res.status(204);
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
