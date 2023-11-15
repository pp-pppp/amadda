import { NextApiRequest, NextApiResponse } from 'next';
import { NextResponse } from 'next/server';

export const init = async (req: NextApiRequest, res: NextApiResponse) => {
  if (req.method !== 'GET')
    return NextResponse.json({ data: 'bad request' }, { status: 400 });

  const AT = req.headers.authorization?.slice(7);

  if (AT) {
    const response = NextResponse.json({ data: 'ok' }, { status: 200 });
    response.cookies.set('Auth', AT, { httpOnly: true, sameSite: 'lax' });
    return response;
  } else {
    return NextResponse.json({ data: 'bad request' }, { status: 400 });
  }
};
