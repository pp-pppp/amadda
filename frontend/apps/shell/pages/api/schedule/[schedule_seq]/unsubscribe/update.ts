import * as Sentry from '@sentry/nextjs';
import type { NextApiRequest, NextApiResponse } from 'next';

import { withAuth, https } from '@amadda/fetch';

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const token = req.headers.authorization || '';
  if (req.method === 'POST') {
    try {
    } catch (err) {
      Sentry.captureException(err);
      return res.status(500).json({ data: 'internal server error' });
    }
  }
  return res.status(400).json({ data: 'bad request' });
};

export default Sentry.wrapApiHandlerWithSentry(withAuth(handler), 'notice/api/schedule/[sehedule_seq]/unsubscribe/update');
