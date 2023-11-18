import React from 'react';
import type { HTMLAttributes } from 'react';
import { COLOR, CURSOR } from './Icon.css';

export interface IconProps extends HTMLAttributes<HTMLDivElement> {
  type: keyof typeof ICON;
  color: keyof typeof COLOR;
  cursor?: keyof typeof CURSOR;
}
export function Icon({ type, color, cursor = 'default' }: IconProps) {
  const className = `${COLOR[color]} ${CURSOR[cursor]}`;
  return (
    <div className={className}>
      <span dangerouslySetInnerHTML={{ __html: ICON[type] }} />
    </div>
  );
}

const ICON = {
  cal: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M231.448-124.078q-25.046 0-42.612-17.916-17.565-17.916-17.565-42.262v-497.103q0-24.347 17.565-42.262 17.566-17.916 42.703-17.916h91.424v-100.616h49.499v100.616h216.614v-100.616h47.961v100.616h91.424q25.137 0 42.703 17.916 17.565 17.915 17.565 42.262v497.103q0 24.346-17.565 42.262-17.566 17.916-42.612 17.916H231.448Zm.091-47.96h496.922q4.615 0 8.462-3.847 3.846-3.846 3.846-8.462v-329.729H219.231v329.729q0 4.616 3.846 8.462 3.847 3.847 8.462 3.847Zm-12.308-389.998h521.538v-119.232q0-4.616-3.846-8.463-3.847-3.846-8.462-3.846H231.539q-4.615 0-8.462 3.846-3.846 3.847-3.846 8.463v119.232Zm0 0v-131.541 131.541Zm261.032 180.305q-12.647 0-21.744-8.832-9.096-8.833-9.096-21.481 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Zm-156 0q-12.647 0-21.744-8.832-9.096-8.833-9.096-21.481 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Zm312 0q-12.647 0-21.744-8.832-9.096-8.833-9.096-21.481 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Zm-156 140.923q-12.647 0-21.744-8.833-9.096-8.833-9.096-21.48 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Zm-156 0q-12.647 0-21.744-8.833-9.096-8.833-9.096-21.48 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Zm312 0q-12.647 0-21.744-8.833-9.096-8.833-9.096-21.48 0-12.648 8.833-21.744t21.481-9.096q12.647 0 21.744 8.833 9.096 8.833 9.096 21.481 0 12.647-8.833 21.743-8.833 9.096-21.481 9.096Z"/></svg>',
  dot: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -500 960 960" width="24"><path d="M480.168-165.731q-30.475 0-51.686-21.047t-21.211-51.528q0-30.27 21.042-51.577 21.043-21.307 51.519-21.307 30.475 0 51.686 21.152t21.211 51.518q0 30.366-21.042 51.577-21.043 21.212-51.519 21.212Z"/></svg>',
  search:
    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="m758.654-167.886-234.897-235q-29.603 22.77-65.088 34.712-35.484 11.942-72.399 11.942-91.486 0-155.32-63.861-63.834-63.862-63.834-155 0-91.137 63.757-155.099 63.756-63.961 154.999-63.961t155.204 63.928q63.961 63.927 63.961 155.195 0 38.292-12.576 73.93-12.577 35.638-34.077 64.066l235 234.418-34.73 34.73ZM386.077-404.192q71.808 0 121.404-49.523 49.596-49.522 49.596-121.5 0-71.978-49.596-121.478t-121.5-49.5q-71.904 0-121.404 49.523t-49.5 121.5q0 71.978 49.516 121.478t121.484 49.5Z"/></svg>',
  config:
    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="m425.232-124.078-21.231-105.423q-17.385-5.846-45.231-20.693-27.847-14.846-58.847-37.654l-100.922 33.077-55.268-96.536 79.999-70.462q-2.577-13.59-4.058-27.603-1.48-14.012-1.48-30.243 0-13.731 1.73-28.443 1.731-14.712 4.116-31.789l-80.307-69.846 55.268-94.959 101.845 33.5q21.654-19.27 47.732-33.866 26.077-14.596 55.039-25.173l21.615-106.538H534.96l21.231 105.807q28.616 10.846 53.443 24.827 24.827 13.981 47.673 34.943l103.885-33.5 55.268 94.959-83.961 72.657q2.961 14.651 4.25 27.92 1.288 13.27 1.288 29.02 0 15.365-1.385 29.026-1.384 13.661-3.461 29.224l82.384 70.539-55.268 96.536-103-34.269q-21.731 19.539-46.039 33.347-24.307 13.807-55.077 25.807L534.96-124.078H425.232Zm37.015-47.96h34.598l19.933-100.654q36.722-7 67.357-24.577 30.634-17.577 55.673-47.731l96.884 33.577 17.885-29.098L677-408q5.5-18.539 8.904-35.994 3.404-17.456 3.404-36.102 0-19.212-3.154-35.789Q683-532.461 677-551.231l78.346-68.961-17.077-29.193-99.154 33.193Q618.962-642.038 584.423-662t-68.115-25.5l-18.572-101.269h-36.121l-17.73 100.59q-38.616 6.717-69.02 23.679-30.403 16.961-57.057 48.308l-95.885-33.193-18.077 29.193 76.077 66.884q-6 17.039-9.5 35.546-3.5 18.508-3.5 37.616 0 19.031 3.25 37.031t8.865 35.923l-75.192 66.577 18.069 29.192 95.2-32q25.885 29.885 57.347 47.5 31.461 17.615 68.952 24.115l18.833 99.77Zm14.43-188.002q50.169 0 84.995-34.867 34.827-34.867 34.827-85.134 0-50.266-34.856-85.093-34.855-34.826-85.105-34.826-49.614 0-84.787 34.867-35.173 34.867-35.173 85.134 0 50.266 35.173 85.093 35.173 34.826 84.926 34.826Zm3.015-120.46Z"/></svg>',
  noti: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M217.348-226.769v-47.961h36.268v-251.193q0-84.5 53.505-148 53.504-63.5 136.495-77.307v-48.33q0-15.154 10.452-25.604 10.453-10.45 25.769-10.45 15.317 0 25.932 10.45 10.615 10.45 10.615 25.604v48.33q83.077 13.807 136.634 77.307 53.558 63.5 53.558 148v251.193h36.269v47.961H217.348ZM480-489.692Zm-.28 371.691q-26.143 0-44.277-18.332-18.135-18.333-18.135-44.36h125.384q0 26.423-18.415 44.558-18.415 18.134-44.557 18.134ZM301.576-274.73h357.04v-251.193q0-74.589-52.059-126.506-52.06-51.918-126.644-51.918-74.585 0-126.461 51.918-51.876 51.917-51.876 126.506v251.193Z"/></svg>',
  noti_red: `<svg version="1.1" id="레이어_1" xmlns="http://www.w3.org/2000/svg"
	xmlns:xlink="http://www.w3.org/1999/xlink" height="24"
	viewBox="0 0 24 24" width="24" style="enable-background:new 0 0 24 24;" xml:space="preserve">
<style type="text/css">
	.st0{fill:#504695;}
	.st1{fill:#E86391;}
</style>
<path class="st0" d="M5.4,18.3v-1.2h0.9v-6.3c0-1.4,0.4-2.6,1.3-3.7c0.9-1.1,2-1.7,3.4-1.9V4c0-0.3,0.1-0.5,0.3-0.6
	c0.2-0.2,0.4-0.3,0.6-0.3c0.3,0,0.5,0.1,0.6,0.3c0.2,0.2,0.3,0.4,0.3,0.6v1.2c1.4,0.2,2.5,0.9,3.4,1.9c0.9,1.1,1.3,2.3,1.3,3.7v6.3
	h0.9v1.2C18.6,18.3,5.4,18.3,5.4,18.3z M12,21c-0.4,0-0.8-0.2-1.1-0.5c-0.3-0.3-0.5-0.7-0.5-1.1h3.1c0,0.4-0.2,0.8-0.5,1.1
	C12.8,20.9,12.4,21,12,21z M7.5,17.1h8.9v-6.3c0-1.2-0.4-2.3-1.3-3.2c-0.9-0.9-1.9-1.3-3.2-1.3c-1.2,0-2.3,0.4-3.2,1.3
	C8,8.6,7.5,9.6,7.5,10.9C7.5,10.9,7.5,17.1,7.5,17.1z"/>
<circle class="st1" cx="16.8" cy="16.8" r="4.4"/>
</svg>`,
  plus: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M458.039-458.539H268.078v-43.922h189.961v-189.961h43.922v189.961h189.961v43.922H501.961v189.961h-43.922v-189.961Z"/></svg>',
  friendadd:
    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M451.423-494.501q21.539-24.653 32.211-54.868 10.673-30.215 10.673-62.092 0-31.528-10.423-61.417-10.422-29.89-32.461-55.543 8-2 15-2.5t15-.5q50.577-1.923 85.865 33.538t35.288 86.422q0 50.961-35.288 86.422t-85.865 33.538q-8 0-15.5-.5t-14.5-2.5Zm187.692 264.307v-60.691q0-36.379-15.596-67.708t-47.057-51.675q60.804 12.707 116.017 37.449 55.212 24.742 55.212 82.098v60.527H639.115Zm120.576-213.922v-83.192h-83.192v-47.961h83.192v-83.192h47.961v83.192h83.192v47.961h-83.192v83.192h-47.961Zm-453.402-47.385q-50.537 0-85.201-34.759-34.663-34.759-34.663-85.201 0-50.442 34.657-85.201 34.658-34.759 85.304-34.759 50.441 0 85.2 34.759 34.76 34.759 34.76 85.201 0 50.442-34.76 85.201-34.759 34.759-85.297 34.759ZM45.656-230.194v-64.075q0-20.038 10.317-36.83 10.316-16.792 29.705-28.873 50.285-31.258 106.846-46.143 56.56-14.884 113.345-14.884t112.766 15.884q55.981 15.885 108.397 45.21 18.275 12.098 29.179 28.863 10.904 16.766 10.904 36.773v64.075H45.656ZM305.89-539.461q29.496 0 50.996-21.004 21.5-21.005 21.5-50.5 0-29.496-21.5-50.996-21.5-21.5-50.996-21.5-29.688 0-50.596 21.5-20.908 21.5-20.908 50.996 0 29.495 20.908 50.5 20.908 21.004 50.596 21.004ZM93.616-278.154h425.539v-16.066q0-7.462-4.539-13.833-4.538-6.371-13.115-12.101-45.193-24.154-94.348-38.52-49.155-14.365-101.267-14.365-52.395 0-101.505 13.865-49.111 13.866-93.303 39.02-8.577 5.73-13.02 12.138-4.442 6.407-4.442 13.747v16.115Zm212.77-333.307Zm0 333.307Z"/></svg>',
  scheduleadd:
    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M696.116-108.116v-108h-108v-47.96h108v-108h47.96v108h108v47.96h-108v108h-47.96ZM231.444-220.078q-24.342 1-42.258-17.416-17.915-18.416-17.915-42.777v-401.073q0-25.062 17.915-42.627 17.916-17.566 42.353-17.566h67.424v-100.616h49.499v100.616h168.614v-100.616h47.961v100.616h67.424q24.437 0 42.853 17.416 18.415 17.415 17.415 42.798V-477q-11.884 2.462-23.98 5.77-12.095 3.307-23.98 8.153v-50.999H219.231v233.729q0 4.616 3.846 8.462 3.847 3.847 8.462 3.847h257.846q-2 10.884-2.808 22.98-.808 12.095-1.192 24.98H231.444Zm-12.213-341.958h425.538v-119.232q0-4.616-3.846-8.463-3.847-3.846-8.462-3.846H231.539q-4.615 0-8.462 3.846-3.846 3.847-3.846 8.463v119.232Zm0 0v-131.541 131.541Z"/></svg>',
  back: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="m312.729-456.116 201.308 201.308L480-220.078 220.078-480 480-739.922l34.037 34.73-201.308 201.116h427.193v47.96H312.729Z"/></svg>',
  friends:
    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M22.117-250.578v-42.646q0-47.682 42.192-74.498 42.192-26.816 114.076-26.816 14.874 0 29.668 1.847 14.794 1.846 30.178 4.994-14.192 17.428-22.038 38.828-7.846 21.4-7.846 42.331v55.96H22.117Zm240 0v-49.04q0-22.985 12.365-42.664 12.365-19.679 33.961-28.486 41.596-18.808 84.2-29.058 42.603-10.25 86.588-10.25 44.831 0 87.434 10.25 42.603 10.25 84.103 29.058 21.5 8.807 34.404 28.308 12.904 19.501 12.904 43.05v48.832H262.117Zm489.536 0v-56.707q0-22.182-8.192-42.525t-22.385-37.843q17.77-3.192 32.058-5.038 14.288-1.847 28.865-1.847 71.885 0 113.98 27.323 42.097 27.322 42.097 73.991v42.646H751.653Zm-442.307-47.96h341.116v-4.231q-2.539-23.462-54.769-41.404-52.231-17.942-115.443-17.942-63.212 0-115.539 17.942t-55.365 41.404v4.231ZM178.081-445.422q-24.272 0-41.157-17.034-16.884-17.034-16.884-40.954 0-24.165 17.034-41.338 17.034-17.172 40.954-17.172 24.165 0 41.53 16.935 17.365 16.936 17.365 41.534 0 23.875-16.917 40.952-16.917 17.077-41.925 17.077Zm603.716 0q-23.99 0-41.066-17.154-17.077-17.155-17.077-40.72 0-24.279 17.154-41.452 17.154-17.172 40.909-17.172 24.474 0 41.647 16.935 17.173 16.936 17.173 41.42 0 24.23-16.888 41.186-16.887 16.957-41.852 16.957Zm-302.301-16.692q-41.765 0-71.495-29.671-29.731-29.671-29.731-72.059 0-42.018 29.672-71.97 29.671-29.951 72.058-29.951 42.019 0 71.97 29.912 29.952 29.913 29.952 72.513 0 41.765-29.913 71.496-29.912 29.73-72.513 29.73Zm.85-47.96q22.4 0 38.008-15.858 15.608-15.858 15.608-38.258 0-22.4-15.514-38.008-15.514-15.607-38.448-15.607-22.054 0-37.912 15.514-15.857 15.514-15.857 38.447 0 22.054 15.857 37.912 15.858 15.858 38.258 15.858Zm-.731 211.536ZM480-563.844Z"/></svg>',
  exc: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M456.116-384.577v-311.96h47.96v311.96h-47.96Zm0 120.922v-47.961h47.96v47.961h-47.96Z"/></svg>',
  up: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="m296-358.463-42.153-42.152L480-626.768l226.153 226.153L664-358.463l-184-184-184 184Z"/></svg>',
  down: '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M480-358.463 253.847-584.615 296-626.768l184 184 184-184 42.153 42.153L480-358.463Z"/></svg>',
};
