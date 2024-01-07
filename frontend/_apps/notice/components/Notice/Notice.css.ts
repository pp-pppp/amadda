import { colors } from 'external-temporal';
import { styleVariants } from '@vanilla-extract/css';
export const BACKGROUND = styleVariants({
  normal: {
    display: 'block',
    padding: '1rem',
    backgroundColor: colors.GREY_100,
    width: 'auto',
    marginBottom: '0.75rem',
    borderRadius: '1rem',
    color: colors.GREY_600,
  },
  isRead: {
    display: 'block',
    padding: '1rem',
    backgroundColor: colors.GREY_200,
    width: 'auto',
    marginBottom: '0.75rem',
    borderRadius: '1rem',
    color: colors.GREY_600,
  },
  notRead: {
    display: 'block',
    padding: '1rem',
    backgroundColor: colors.GREY_100,
    width: 'auto',
    color: colors.key,
    marginBottom: '0.75rem',
    borderRadius: '1rem',
  },
});
