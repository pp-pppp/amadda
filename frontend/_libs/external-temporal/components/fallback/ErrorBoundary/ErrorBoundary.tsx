import React from 'react';
import { Component, type ErrorInfo, type ReactNode } from 'react';
import { BASE } from './ErrorBoundary.css';
import * as Sentry from '@sentry/nextjs';
import { H3 } from '#/components/typography/Hn/H3';
export interface ErrorBoundaryProps {
  fallback?: ReactNode;
  message?: string;
  children?: ReactNode;
}
interface ErrorBoundaryState {
  hasError: boolean;
}
export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false };
  }
  static getDerivedStateFromError(error: Error) {
    return { hasError: true };
  }
  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    Sentry.captureException(error);
  }
  render() {
    if (this.state.hasError) {
      return this.props.fallback ? (
        this.props.fallback
      ) : (
        <div className={BASE}>
          <H3>{this.props.message ? this.props.message : '에러가 발생했어요. 다시 한 번 시도해주세요.'}</H3>
        </div>
      );
    }
    // Return children components in case of no error
    return this.props.children;
  }
}
