/**
 * @jest-environment node
 */

import { NextRequest, NextResponse } from 'next/server';
import { injectCookie } from '@/middleware';
import tokenValidation from '@/middleware';
import axios from 'axios';
import { REDIS } from '@/connection/connection';

jest.mock('axios', () => ({
  get: jest.fn(),
}));
jest.mock('@/connection/connection', () => ({
  REDIS: {
    getRefreshToken: jest.fn(),
    setRefreshToken: jest.fn(),
  },
}));

describe('I. 클라이언트 요청에 대한 토큰 검증 로직을 테스트합니다.', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  test('1. 요청 쿠키에 액세스 토큰이 없을 경우 로그아웃 API로 rewrite해야 합니다.', async () => {
    const req = new NextRequest('http://exampleapiroute.com', {
      headers: new Headers(),
      method: 'GET',
    });
    const res = await tokenValidation(req);
    expect(res).toBeInstanceOf(NextResponse);
    expect(res.headers.get('x-middleware-rewrite')).toContain(
      '/api/auth/signout'
    );
  });

  test('2. 프리플라이트 요청 결과 Access Token에 이상이 없다면 요청을 Spring 서버로 전송해야 합니다.', async () => {
    const req = new NextRequest('http://exampleapiroute.com', {
      headers: new Headers(),
      method: 'POST',
    });
    req.cookies.set('at', 'valid-access-token');
    (axios.get as jest.Mock).mockReturnValue(
      Promise.resolve({ data: { isBroken: false, isExpired: false } })
    );
    const res = await tokenValidation(req);
    expect(res).toBeInstanceOf(NextResponse);
  });

  test('3. 프리플라이트 요청 결과 Access Token이 손상(Broken)되었을 경우 로그아웃 API로 rewrite해야 합니다.', async () => {
    const req = new NextRequest('http://exampleapiroute.com', {
      headers: new Headers(),
      method: 'GET',
    });
    req.cookies.set('at', 'broken-access-token');
    (axios.get as jest.Mock).mockReturnValue(
      Promise.resolve({ data: { isBroken: true } })
    );
    const res = await tokenValidation(req);
    expect(res).toBeInstanceOf(NextResponse);
    expect(res.headers.get('x-middleware-rewrite')).toContain(
      '/api/auth/signout'
    );
  });

  test('4. 프리플라이트 요청 결과 Access Token이 만료(Expired)되었을 경우 Refresh Token으로 다시 프리플라이트 요청을 보내야 합니다. 성공한 경우 새 액세스 토큰으로 요청을 다시 보내야 합니다.', async () => {
    const req = new NextRequest('http://exampleapiroute.com', {
      headers: new Headers(),
      method: 'GET',
    });
    req.cookies.set('at', 'expired-access-token');
    (axios.get as jest.Mock).mockReturnValueOnce(
      Promise.resolve({
        data: {
          isExpired: true,
          isBroken: false,
          refreshAccessKey: 'refresh-key',
        },
      })
    );
    (REDIS.getRefreshToken as jest.Mock).mockReturnValue('valid-refresh-token');
    (axios.get as jest.Mock).mockReturnValueOnce(
      Promise.resolve({
        status: 200,
        data: {
          accessToken: 'new-access-token',
          refreshToken: 'new-refresh-token',
          refreshAccessKey: 'new-refresh-key',
        },
      })
    );

    const res = await tokenValidation(req);

    expect(res).toBeInstanceOf(NextResponse);
    expect(res.headers.get('Auth')).toBe('new-access-token');
  });

  test('5. 리프레시 토큰으로 보낸 프리플라이트 요청이 실패한 경우 로그아웃 API로 rewrite해야 합니다.', async () => {
    const req = new NextRequest('http://exampleapiroute.com', {
      headers: new Headers(),
      method: 'GET',
    });
    req.cookies.set('at', 'expired-access-token');
    (axios.get as jest.Mock).mockReturnValueOnce(
      Promise.resolve({
        data: {
          isExpired: true,
          isBroken: false,
          refreshAccessKey: 'refresh-key',
        },
      })
    );
    (REDIS.getRefreshToken as jest.Mock).mockReturnValue('weird-refresh-token');
    (axios.get as jest.Mock).mockReturnValueOnce(
      Promise.resolve({
        status: 401,
        data: {},
      })
    );
    const res = await tokenValidation(req);
    expect(res).toBeInstanceOf(NextResponse);
    expect(res.headers.get('x-middleware-rewrite')).toContain(
      '/api/auth/signout'
    );
  });
});

describe('II. Spring 서버로부터 오는 토큰을 httpOnly 쿠키에 넣어 클라이언트로 내려보내는 로직을 테스트합니다.', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  test('1. 서버로부터 온 토큰이 HttpOnly 쿠키로 옮겨져야 합니다.', async () => {
    const response = NextResponse.rewrite('http://some-url');
    response.headers.set('Auth', 'access-token');
    const result = await injectCookie(response);
    expect(result.cookies.get('at')?.httpOnly).toBe(true);
    expect(result.cookies.get('at')?.value).toBe('access-token');
  });
});
