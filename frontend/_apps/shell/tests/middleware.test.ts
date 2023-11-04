import { tokenValidation } from '@/pages/middleware';
import { NextApiResponse } from 'next';
import { NextRequest, NextResponse } from 'next/server';

describe('미들웨어가 잘 작동하는지 테스트합니다', () => {
  const mockNextRequest1 = {
    cookies: {},
  } as unknown as NextRequest;

  const mockNextRequest2 = {
    cookies: {
      at: 'broken-access-token',
    },
    isBroken: true,
  } as unknown as NextRequest;

  const brokenATReq = {
    cookies: {
      at: 'broken-access-token',
    },
  } as unknown as NextRequest;

  const brokenATRes = {
    headers: {
      Auth: 'broken-access-token',
    },
    isBroken: true,
  } as unknown as NextResponse;

  const mockNextResponse1 = {
    headers: {
      Auth: 'valid-access-token',
    },
    isBroken: false,
    isExpired: true,
  } as unknown as NextResponse;

  const mockNextResponse2 = {
    cookies: {
      at: 'valid-access-token',
    },
    headers: {
      Auth: 'valid-access-token',
    },
  } as unknown as NextResponse;

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('쿠키에 토큰이 없으면 로그아웃되어야 합니다.', async () => {
    (mockNextRequest1.cookies.has as jest.Mock).mockReturnValue(false); //쿠키에 토큰이 없음이 확인된 상황이면
    const req = tokenValidation(mockNextRequest1);
    expect(await req).toBeInstanceOf(NextRequest);
    expect((await req).headers.get('at')).toBe('valid-access-token');
  });

  test('Access Token이 유효하다면, request가 그대로 리턴되어서 Spring으로 전달되어야 합니다.', async () => {
    (tokenValidation as jest.Mock).mockReturnValue;
    const req = tokenValidation(mockNextRequest2);
    expect(await req).toBeInstanceOf(NextRequest);
    expect((await req).headers.get('at')).toBe('valid-access-token');
  });
  test('확인받은 Access Token이 Broken이라면, 로그아웃하도록 해야 합니다.', async () => {
    // const data = await gateway();
    // expect(data).toBe('')
    (tokenValidation as jest.Mock).mockReturnValue(brokenATRes);
    const res = tokenValidation(brokenATReq);
    expect(await res).toBeInstanceOf(NextResponse);
    // expect((await res).isBroken).toBe(true);
    expect((await res).headers.get('x-middleware-rewrite')).toBe(
      'http://localhost:3000/api/auth/signout'
    );
  });
  test('확인받은 Refresh Token이 유효하다면, 재발급받은 Access Token을 주입해 API 응답 결과를 리턴해야 합니다.', async () => {
    // const data = await gateway();
    // expect(data).toBe('')
    const res = tokenValidation(mockNextRequest2);
    // expect(res.headers.get('Auth')).toBe('new-valid-access-token');
    // expect(res.status).toBe(200);
  });

  test('Refresh Token의 확인 결과가 Unauthrorized라면, 로그아웃하도록 해야 합니다.', async () => {
    // const data = await gateway();
    // expect(data).toBe('')
    // expect(res.status).toBe(302);
    // expect(res.headers.get('x-middleware-rewrite')).toBe(
    //   'http://localhost:3000/api/auth/signout'
    // );
  });
});
