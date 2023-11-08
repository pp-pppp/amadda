import Axios, { AxiosResponse } from 'axios';
export const ROOT = process.env.SPRINT_ROOT as string;

const axios = Axios.create({
  baseURL: ROOT,
});

export const http = {
  get: <Response = unknown>(url: string) =>
    axios.get<Response>(url).then(res => res),
  post: <Request = unknown, Response = unknown>(url: string, body?: Request) =>
    axios.post<Response>(url, body).then(res => res),
  patch: <Request = unknown, Response = unknown>(url: string, body?: Request) =>
    axios.patch<Response>(url, body).then(res => res),
  delete: <Response = unknown>(url: string) =>
    axios.delete<Response>(url).then(res => res),
};
