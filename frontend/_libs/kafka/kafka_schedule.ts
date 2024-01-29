import { Kafka } from 'kafkajs';
import * as Sentry from '@sentry/nextjs';

const groupId = process.env.KAFKA_CLIENT_GROUP_ID as string;

const scheduleReload = 'prod.reload.schedule';
const kafka = new Kafka({
  clientId: `${process.env.KAFKA_CLIENT_ID}schedule`,
  brokers: [process.env.KAFKA_BROKER_1, process.env.KAFKA_BROKER_2, process.env.KAFKA_BROKER_3] as string[],
});

export const KAFKA_SCHEDULE = kafka.consumer({ groupId: groupId });
try {
  KAFKA_SCHEDULE.connect();
  KAFKA_SCHEDULE.subscribe({
    topics: [scheduleReload],
  });
} catch (err) {
  //TODO: 중복 구독 처리
  Sentry.captureException(err);
}
