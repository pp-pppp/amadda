import axios from 'axios';

export const https = {
  get: <Response = unknown>(url: string, token: string) =>
    axios
      .get<Response>(url, {
        headers: {
          Authorization: `${token}`,
        },
      })
      .then(res => res),
  post: <Request = unknown, Response = unknown>(
    url: string,
    token: string,
    body?: Request
  ) =>
    axios
      .post<Response>(url, body, {
        headers: {
          Authorization: `${token}`,
        },
      })
      .then(res => res),
  put: <Request = unknown, Response = unknown>(
    url: string,
    token: string,
    body?: Request
  ) =>
    axios
      .put<Response>(url, body, {
        headers: {
          Authorization: `${token}`,
        },
      })
      .then(res => res),
  patch: <Request = unknown, Response = unknown>(
    url: string,
    token: string,
    body?: Request
  ) =>
    axios
      .patch<Response>(url, body, {
        headers: {
          Authorization: `${token}`,
        },
      })
      .then(res => res),
  delete: <Response = unknown>(url: string, token: string) =>
    axios
      .delete<Response>(url, {
        headers: {
          Authorization: `${token}`,
        },
      })
      .then(res => res),
};
