import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Page from '../pages';

describe('테스트 테스트', () => {
  it('페이지에 "hi"라는 문자열이 있어야 합니다', () => {
    const page = render(<Page />);

    const home = screen.getByText('hi');
    expect(home).toBeInTheDocument();
    expect(page).toMatchSnapshot();
  });
});
