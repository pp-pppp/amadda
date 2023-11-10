import type { Meta, StoryObj } from '@storybook/react';
import { MobileDailyPlate } from '../../../schedule/components/MobileDailyPlate/MobileDailyPlate';

const meta = {
  title: 'Schedule/MobileDailyPlate',
  component: MobileDailyPlate,
  parameters: {
    layout: 'centered',
    componentSubtitle: '하루 일정...뷰.....',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof MobileDailyPlate>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    MobileDailyPlateProps: {
      color: 'hotpink',
      scheduleName: '업무 미팅',
      person: 7,
      participants: [
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
      ],
      startTime: '09:00',
      endTime: '11:00',
    },
  },
};

export const Zero: Story = {
  args: {
    MobileDailyPlateProps: {
      color: 'hotpink',
      scheduleName: '업무 미팅',
      person: 1,
      startTime: '09:00',
      endTime: '11:00',
    },
  },
};

export const Over: Story = {
  args: {
    MobileDailyPlateProps: {
      color: 'hotpink',
      scheduleName: '업무 미팅',
      person: 3,
      participants: [
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
      ],
      startTime: '09:00',
      endTime: '11:00',
    },
  },
};
