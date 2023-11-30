import React from 'react';
import { Component, type ErrorInfo, type ReactNode } from 'react';
import { BASE } from './ErrorBoundary.css';
import { H2 } from '#/components/typography/Hn/H2';
import { H3 } from '#/components/typography/Hn/H3';
export interface ErrorBoundaryProps {
  children?: ReactNode;
}
interface ErrorBoundaryState {
  hasError: boolean;
}
export default class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false };
  }
  static getDerivedStateFromError(error: Error) {
    return { hasError: true };
  }
  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    // You can use your own error logging service here
    console.log({ error, errorInfo });
  }
  render() {
    // Check if the error is thrown
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return (
        <div className={BASE}>
          <H3>죄송해요! 잠시 후 다시 시도해주세요.</H3>
        </div>
      );
    }

    // Return children components in case of no error

    return this.props.children;
  }
}
