import MobileMonthPlate from '@/components/mobile/MonthlyPlate/MonthlyMobilePlate';
import { P } from 'external-temporal';

export default function Page() {
  return (
    <div>
      <P>Schedule Module(port: 3002)</P>
      {Array.from({ length: 31 }, (_, i) => {
        const dateType = (i + 1) % 7 > 4 ? 'weekend' : 'weekday';
        return (
          <MobileMonthPlate dateType={dateType}>
            {(i + 1).toString()}
          </MobileMonthPlate>
        );
      })}
    </div>
  );
}
