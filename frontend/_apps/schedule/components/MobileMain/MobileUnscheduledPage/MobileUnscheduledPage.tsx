import { MobileDailyPlate } from '@SCH/components/MobileDailyPlate/MobileDailyPlate';
import { MobileDailyPlateList } from '@SCH/components/MobileDailyPlateList/MobileDailyPlateList';
import { Spacing } from 'external-temporal';
import { ReactNode } from 'react';

export function MobileUnscheduledPage(): ReactNode {
  return (
    <>
      <MobileDailyPlateList>
        <MobileDailyPlate
          color="grey"
          scheduleName="민병기와 식사하기 이 오빠 정신없어보여서 밥 사달라고도 못하겠고 밥먹자고도"
          participants={[
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
          ]}
          person={7}
        />
        <Spacing dir="v" size="0.5" />
        <MobileDailyPlate
          color="salmon"
          scheduleName="정민영이 삼성에 붙어서 취업턱을 낸다면 이 날짜가 정해지겠지?"
          person={4}
          participants={[
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
            'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-16-13-14-18.jpg',
          ]}
        />
        <Spacing dir="v" size="0.5" />
        <MobileDailyPlate
          color="hotpink"
          scheduleName="내 인생 종료"
          person={2}
        />
        <Spacing dir="v" size="0.5" />
      </MobileDailyPlateList>
    </>
  );
}
