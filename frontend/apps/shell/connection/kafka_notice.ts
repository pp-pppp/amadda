import { Kafka } from 'kafkajs';
import * as Sentry from '@sentry/nextjs';

const groupId = process.env.KAFKA_CLIENT_GROUP_ID as string;
const friendRequest = 'prod.alarm.friend-request';
const friendAccept = 'prod.alarm.friend-accept';
const scheduleAssigned = 'prod.alarm.schedule-assigned';
const scheduleUpdate = 'prod.alarm.schedule-update';
const scheduleNotification = 'prod.alarm.schedule-notification';
const kafka = new Kafka({
  clientId: `${process.env.KAFKA_CLIENT_ID}notice`,
  brokers: [process.env.KAFKA_BROKER_1, process.env.KAFKA_BROKER_2, process.env.KAFKA_BROKER_3] as string[],
});

export const KAFKA_NOTICE = kafka.consumer({ groupId: groupId });
try {
  // KAFKA_NOTICE.connect();
  // KAFKA_NOTICE.subscribe({
  //   topics: [friendRequest, friendAccept, scheduleAssigned, scheduleUpdate, scheduleNotification],
  // });
} catch (err) {
  Sentry.captureException(err);
}
