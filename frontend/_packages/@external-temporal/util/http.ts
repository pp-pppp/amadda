import Axios, { AxiosResponse } from 'axios';
export const ROOT = process.env.SPRINT_ROOT as string;

const axios = Axios.create({
  baseURL: ROOT,
});

export const http = {
  get: <Response = unknown>(url: string) =>
    axios.get<Response>(url).then(res => res.data),
  post: <Request = unknown, Response = AxiosResponse>(
    url: string,
    body?: Request
  ) => axios.post<Response>(url, body).then(res => res.data),
  patch: <Request = unknown, Response = unknown>(url: string, body?: Request) =>
    axios.patch<Response>(url, body).then(res => res.data),
  delete: <Response = unknown>(url: string) =>
    axios.delete<Response>(url).then(res => res.data),
};
