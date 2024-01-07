import { Kafka } from 'kafkajs';
import * as Sentry from '@sentry/nextjs';

const groupId = process.env.KAFKA_CLIENT_GROUP_ID as string;

const scheduleReload = 'prod.reload.schedule';
const kafka = new Kafka({
  clientId: `${process.env.KAFKA_CLIENT_ID}schedule`,
  brokers: [process.env.KAFKA_BROKER_1, process.env.KAFKA_BROKER_2, process.env.KAFKA_BROKER_3] as string[],
});

const schedule_consumer = kafka.consumer({ groupId: groupId });
export const KAFKA_SCHEDULE = async () => {
  console.log('kafka-start subscribe');
  try {
    await schedule_consumer.connect();
    await schedule_consumer.subscribe({
      topics: [scheduleReload],
    });
    return schedule_consumer;
  } catch (err) {
    Sentry.captureException(err);
  }
};
