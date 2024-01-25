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

const notice_consumer = kafka.consumer({ groupId: groupId });
export const KAFKA_NOTICE = async () => {
  console.log('kafka-start subscribe');

  try {
    await notice_consumer.connect();
    await notice_consumer.subscribe({
      topics: [friendRequest, friendAccept, scheduleAssigned, scheduleUpdate, scheduleNotification],
    });
  } catch (err) {
    //TODO: 중복 구독 처리
    Sentry.captureException(err);
  } finally {
    return notice_consumer;
  }
};
