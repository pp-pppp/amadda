import { Kafka } from 'kafkajs';

const groupId = process.env.KAFKA_CLIENT_GROUP_ID as string;
const friendRequest = 'prod.alarm.friend-request';
const friendAccept = 'prod.alarm.friend-accept';
const scheduleAssigned = 'prod.alarm.schedule-assigned';
const scheduleUpdate = 'prod.alarm.schedule-update';
const scheduleNotification = 'prod.alarm.schedule-notification';
const kafka = new Kafka({
  clientId: process.env.KAFKA_CLIENT_ID,
  brokers: [
    process.env.KAFKA_BROKER_1,
    process.env.KAFKA_BROKER_2,
    process.env.KAFKA_BROKER_3,
  ] as string[],
});

const consumer = kafka.consumer({ groupId: groupId });
export const KAFKA = async () => {
  console.log('kafka-start subscribe');

  await consumer.connect();
  await consumer.subscribe({
    topics: [
      'test',
      friendRequest,
      friendAccept,
      scheduleAssigned,
      scheduleUpdate,
      scheduleNotification,
    ],
  });
  return consumer;
};
