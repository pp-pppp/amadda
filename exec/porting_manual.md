# A208 포팅매뉴얼

## 프로젝트 정보
### EC2(xlarge)
- CPU: 4vCPUs
- RAM: 16GB
- SSD: 320GB

### 기술 스택
#### 프론트엔드
- TypeScript 5.2.2
- React 18.2.0
- Next.js 13.4.12
- Vanilla Extract 1.13.0
- Storybook 7.5.1
- Webpack 5.80.0
- axios 1.6.0
- SWR 2.2.4
- zustand 4.4.6
- kafkajs 2.2.4
- redis 4.6.10
- pnpm 8.6.10
- turbo latest
- Jest 29.7
- Testing-library/react 14.0.0
- next-auth 4.24.4
#### 백엔드
- Java 17
- Spring Boot 3.1.4
- Spring Data JPA + Hibernate
- JWT
- JUnit5 + Mockito5 + H2 Database
- Apache Kafka
- MySQL 8.0.33
- AWS S3
#### 인프라
- Docker
- Docker Compose
- Nginx
- Jenkins

## 방화벽 설정 (ufw-docker)
```
sudo wget -O /usr/local/bin/ufw-docker \
https://github.com/chaifeng/ufw-docker/raw/master/ufw-docker
sudo chmod +x /usr/local/bin/ufw-docker

mkdir ~/backup
sudo cp /etc/uft/after.rules ~/backup

sudo ufw-docker install
sudo systemctl restart ufw

sudo ufw-docker allow <컨테이너 이름> <포트>
```
![ufw status](https://github.com/0minyoung0/problem-solving/assets/122426037/a1ec0343-e0af-41f9-8ebe-4d8c07469069)

## Domain 설정 (Gabia)
![Gabia DNS 관리](https://github.com/0minyoung0/problem-solving/assets/122426037/637f4069-9ed4-46d2-810d-ef0a1487d9c9)

## Nginx 설정 (HTTPS 및 HTTP2.0 적용)
/etc/nginx/conf.d/default.conf
```
upstream backend {
  server 0.0.0.0:8080;
}

upstream shell {
  server 0.0.0.0:3000;
}

upstream user {
  server 0.0.0.0:3001;
}

upstream schedule {
  server 0.0.0.0:3002;
}

upstream notice {
  server 0.0.0.0:3003;
}

upstream remote-cache {
  server 0.0.0.0:3100;
}

server {
  server_name {server_name};
  return 301 $scheme://amadda.co.kr$request_uri;

  listen [::]:443 ssl; # managed by Certbot
  listen 443 ssl; # managed by Certbot
  ssl_certificate /etc/letsencrypt/live/amadda.co.kr/fullchain.pem; # managed by Certbot
  ssl_certificate_key /etc/letsencrypt/live/amadda.co.kr/privkey.pem; # managed by Certbot
  include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
  ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}

server {

  server_name amadda.co.kr www.amadda.co.kr;

  location = /bpi/healthcheck {
    rewrite ^/bpi/healthcheck /actuator/health break;
    proxy_pass http://backend;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location /bpi {
    rewrite ^/bpi(.*)$ /api$1 break;
    proxy_pass http://backend;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location = /cache {
    rewrite ^/cache(.*)$ $1 break;
    proxy_pass http://remote-cache;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location /mf/user {
    proxy_pass http://user;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location /mf/schedule {
    proxy_pass http://schedule;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location /mf/notice {
    proxy_pass http://notice;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  location / {
    proxy_pass http://shell;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
  }

  listen [::]:443 ssl http2 ipv6only=on; # managed by Certbot
  listen 443 ssl http2; # managed by Certbot
  ssl_certificate /etc/letsencrypt/live/amadda.co.kr/fullchain.pem; # managed by Certbot
  ssl_certificate_key /etc/letsencrypt/live/amadda.co.kr/privkey.pem; # managed by Certbot
  include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
  ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}


server {
  if ($host = www.amadda.co.kr) {
    return 301 https://$host$request_uri;
  } # managed by Certbot

  if ($host = amadda.co.kr) {
    return 301 https://$host$request_uri;
  } # managed by Certbot

  listen 80;
  listen [::]:80;

  server_name amadda.co.kr www.amadda.co.kr;
  return 404; # managed by Certbot
}

server {
  if ($host = amadda.co.kr) {
    return 301 https://$host$request_uri;
  } # managed by Certbot

  listen 80;
  listen [::]:80;
  server_name {server_name};
  return 404; # managed by Certbot
}
```
/etc/nginx/conf.d/jenkins.conf
```
upstream jenkins {
  keepalive 32; # keepalive connections
  server 127.0.0.1:8088; # Jenkins의 IP와 Port 필요. 이 튜토리얼에선 8088 사용
}

# Required for Jenkins websocket agents
map $http_upgrade $connection_upgrade {
  default upgrade;
  '' close;
}

server { # Listen on port 80 for IPv4 requests

  server_name jenkins.amadda.co.kr;

  # this is the jenkins web root directory
  # (mentioned in the output of "systemctl cat jenkins")
  root /var/run/jenkins/war/;

  access_log /var/log/nginx/jenkins.access.log;
  error_log /var/log/nginx/jenkins.error.log;

  # pass through headers from Jenkins that Nginx considers invalid
  ignore_invalid_headers off;

  location ~ "^/static/[0-9a-fA-F]{8}\/(.*)$" {
    # rewrite all static files into requests to the root
    # E.g /static/12345678/css/something.css will become /css/something.css
    rewrite "^/static/[0-9a-fA-F]{8}\/(.*)" /$1 last;
  }

  location /userContent {
    # have nginx handle all the static requests to userContent folder
    # note : This is the $JENKINS_HOME dir
    root /var/lib/jenkins/;
    if (!-f $request_filename){
      # this file does not exist, might be a directory or a /**view** url
      rewrite (.*) /$1 last;
      break;
    }
    sendfile on;
  }

  location / {
    sendfile off;
    proxy_pass http://jenkins;
    proxy_redirect default;
    proxy_http_version 1.1;

    # Required for Jenkins websocket agents
    proxy_set_header Connection $connection_upgrade;
    proxy_set_header Upgrade $http_upgrade;

    proxy_set_header Host $http_host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_max_temp_file_size 0;

    #this is the maximum upload size
    client_max_body_size 10m;
    client_body_buffer_size 128k;

    proxy_connect_timeout 90;
    proxy_send_timeout 90;
    proxy_read_timeout 90;
    proxy_buffering off;
    proxy_request_buffering off; # Required for HTTP CLI commands
    proxy_set_header Connection ""; # Clear for keepalive
  }

  listen 443 ssl http2; # managed by Certbot
  ssl_certificate /etc/letsencrypt/live/amadda.co.kr/fullchain.pem; # managed by Certbot
  ssl_certificate_key /etc/letsencrypt/live/amadda.co.kr/privkey.pem; # managed by Certbot
  include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
  ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}

server {
  listen 80;
  server_name jenkins.amadda.co.kr;
  return 404; # managed by Certbot
}

server {
  if ($host = jenkins.amadda.co.kr) {
    return 301 https://$host$request_uri;
  } # managed by Certbot

  server_name jenkins.amadda.co.kr;
  listen 80;
  return 404; # managed by Certbot
}
```

## Jenkins
```
df -h # 용량 할당
sudo fallocate -l 32G /swapfile # Swap 영역 할당 (일반적으로 서버 메모리의 2배)
sudo chmod 600 /swapfile # Swapfile 권한 수정
sudo mkswap /swapfile # Swapfile 생성
sudo swapon /swapfile # Swapfile 활성화
free -h # swap 영역이 할당 되었는지 확인
```
```
sudo docker pull jenkins/jenkins:lts
```
```
sudo docker run -d \
--env JENKINS_OPTS=--httpPort=8088 \
--env JAVA_OPTS=-Xmx2g \
-v /etc/localtime:/etc/localtime:ro \
-e TZ=Asia/Seoul \
-p 8088:8088 \
-v /jenkins:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /usr/bin/docker-compose:/usr/bin/docker-compose \
--name jenkins \
-u root \
jenkins/jenkins:lts
```

## Git WebHook 설정
```
  stage('Git Clone') {
    steps {
      git branch: 'develop', url: "${env.giturl}", credentialsId: 'github-access-token'
    }
  }
```

## next.js TurboRepo CI/CD
Dockerfile
```
FROM node:20-alpine AS base
ARG service_name
ARG service_port
ARG CACHE_PATH=/cache/node
RUN npm install -g pnpm
RUN npm install -g turbo
RUN pnpm config set store-dir ${CACHE_PATH}/.pnpm-store
WORKDIR /app

FROM base AS separator
WORKDIR /app
COPY . .
RUN turbo prune --scope=${service_name} --docker

FROM base AS installer
WORKDIR /app
COPY .gitignore .gitignore
COPY --from=separator /app/out/json/. .
COPY --from=separator /app/out/pnpm-lock.yaml ./pnpm-lock.yaml
RUN pnpm install --frozen-lockfile

FROM base AS builder
WORKDIR /app
COPY --from=installer /app/. .
COPY --from=separator /app/out/full/. .
RUN turbo run build --filter=${service_name}

FROM base AS runner
WORKDIR /app
COPY --from=builder app/. .
WORKDIR /app/_apps/${service_name}
EXPOSE ${service_port}
CMD ["pnpm", "start"]
```
docker-compose.yml
```
version: '3'

services:
  next-shell:
    container_name: amadda-next-shell
    image: amadda-fe-shell
    build:
      context: ./frontend
      dockerfile: Dockerfile
      target: runner
      args:
        - service_name=shell
        - service_port=3000
    volumes:
      - output-volume:/cache/node
    ports:
      - '3000:3000'
    environment:
      - TZ=Asia/Seoul
      - CI=1
      - TURBO_API=http://amadda.co.kr/cache
      - TURBO_TOKEN=
      - TURBO_TEAM=
    restart: on-failure:5
    networks:
      - deploy

  next-user:
    container_name: amadda-next-user
    image: amadda-fe-user
    build:
      context: ./frontend
      dockerfile: Dockerfile
      target: runner
      args:
        - service_name=user
        - service_port=3001
    volumes:
      - output-volume:/cache/node
    ports:
      - '3001:3001'
    environment:
      - TZ=Asia/Seoul
      - CI=1
      - TURBO_API=http://amadda.co.kr/cache
      - TURBO_TOKEN=
      - TURBO_TEAM=
    restart: on-failure:5
    networks:
      - deploy

  next-schedule:
    container_name: amadda-next-schedule
    image: amadda-fe-schedule
    build:
      context: ./frontend
      dockerfile: Dockerfile
      target: runner
      args:
        - service_name=schedule
        - service_port=3002
    volumes:
      - output-volume:/cache/node
    ports:
      - '3002:3002'
    environment:
      - TZ=Asia/Seoul
      - CI=1
      - TURBO_API=http://amadda.co.kr/cache
      - TURBO_TOKEN=
      - TURBO_TEAM=
    restart: on-failure:5
    networks:
      - deploy

  next-notice:
    container_name: amadda-next-notice
    image: amadda-fe-notice
    build:
      context: ./frontend
      dockerfile: Dockerfile
      target: runner
      args:
        - service_name=notice
        - service_port=3003
    volumes:
      - output-volume:/cache/node
    ports:
      - '3003:3003'
    environment:
      - TZ=Asia/Seoul
      - CI=1
      - TURBO_API=http://amadda.co.kr/cache
      - TURBO_TOKEN=
      - TURBO_TEAM=
    restart: on-failure:5
    networks:
      - deploy

networks:
  deploy:
    external: true

volumes:
  output-volume:
    external: true
```
Jenkins Pipeline
```
pipeline {
  agent any
  stages {
    stage('Git Clone') {
      steps {
        git branch: 'develop', url: "${giturl}", credentialsId: 'github-access-token'
      }
    }
    
    stage("Set environment") {
      steps {
        echo "Copy require file to pipeline folder"
        sh 'cp /var/jenkins_home/util/amadda_frontend/docker-compose.yml ./'
        sh 'cp /var/jenkins_home/util/amadda_frontend/.env ./frontend/.env'
        sh 'cp /var/jenkins_home/util/amadda_frontend/Dockerfile ./frontend/'
      }
    }
    
    stage('Docker down') {
      steps {
        echo "Docker compose down"
        sh "docker-compose -f docker-compose.yml down --rmi all"
      }
    }
    
    stage('Build') {
      steps {
        echo "docker compose build"
        sh "docker-compose -f docker-compose.yml build"
      }
      post {
        success {
          echo "Success to build"
        }
        failure {
          echo "Docker build failed. clear unused file"
          sh "docker system prune -f"
          error 'pipeline aborted'
        }
      }
    }
    
    stage('Docker up') {
      steps {
        echo "docker compose up"
        sh "docker-compose -f docker-compose.yml up -d"
      }
    }
    
    stage('Docker clear') {
      steps {
        sh 'docker system prune -f'
      }
    }
  }
}
```

## SpringBoot CI/CD
Dockerfile
```
FROM gradle:8.3-jdk17-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle /app/
RUN gradle clean build -Pprofile=dev -x test --parallel --continue > /dev/null 2>&1 || true
COPY ./ ./
RUN gradle build -Pprofile=dev -x test --parallel

FROM openjdk:17-jdk AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
```
docker-compose.yml
```
version: '3'

services:
  springboot:
    container_name: amadda-springboot
    image: amadda-be
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - '8080:8080'
    environment:
      - TZ=Asia/Seoul
    restart: on-failure:5
    networks:
      - deploy

networks:
  deploy:
    external: true
```
Jenkins Pipeline
```
pipeline {
  agent any
  environment {
    HEALTH_CHECK_API = 'http://amadda.co.kr/bpi/healthcheck'
  }
  stages {    
    stage('Git Clone') {
      steps {
        git branch: 'develop', url: "${giturl}", credentialsId: 'github-access-token'
      }
    }
      
    stage("Set environment") {
      steps {
        echo "Copy require file to pipeline folder"
        sh 'cp /var/jenkins_home/util/amadda_backend/docker-compose.yml ./'
        sh 'cp /var/jenkins_home/util/amadda_backend/env.yml ./backend/src/main/resources/env.yml'
        sh 'cp /var/jenkins_home/util/amadda_backend/Dockerfile ./backend/'
      }
    }
      
    stage('test') {
      steps {
        dir('backend') {
          sh 'chmod +x ./gradlew'
          sh './gradlew test'
        }
      }
    }
      
    stage('Docker down') {
      steps {
        echo "Docker compose down"
        sh "docker-compose -f docker-compose.yml down --rmi all"
      }
    }
      
    stage('Build') {
      steps {
        echo "docker compose build"
        sh "docker-compose -f docker-compose.yml build"
      }
      post {
        success {
          echo "Success to build"
        }
        failure {
          echo "Docker build failed. clear unused file"
          sh "docker system prune -f"
          error 'pipeline aborted'
        }
      }
    }
      
    stage('Docker up') {
      steps {
        echo "docker compose up"
        sh "docker-compose -f docker-compose.yml up -d"
      }
    }
    
    stage('HealthCheck') {
      steps {
        script {
          for (int i = 0; i < 20; i++) {
            try {
              sh 'curl -s "${HEALTH_CHECK_API}" > /dev/null'
              currentBuild.result = 'SUCCESS'
              break
            } catch (Exception e) {
              if (i == 19) {
                currentBuild.result = 'FAILURE'
              } else {
                echo "The server is not alive yet. Retry health check in 5 seconds..."
                sleep(5)
              }
            }
          }
        }
      }
      post {
        failure {
          echo "Release Fail. clear unused file"
          sh "sudo docker system prune -f"
          error 'pipeline aborted'
        }
        success {
          echo "Release Success"
        }
      }
    }
    
    stage('Docker clear') {
        steps {
            sh 'docker system prune -f'
        }
    }
  }
}
```

## MySQL
docker-compose.yml
```
version: "3"
services:
  mysql:
    image: mysql:8.0.33
    container_name: amadda-mysql 
    restart: always
    environment:
      TZ: Asia/Seoul
      MYSQL_ROOT_PASSWORD:
      MYSQL_USER: 
      MYSQL_PASSWORD:
      MYSQL_DATABASE: 
      MYSQL_CHARACTER_SET_SERVER: utf8mb4
      MYSQL_COLLATION_COLLATION_SERVER: utf8mb4_unicode_ci
    ports:
      - 3306:3306
    volumes:
      - amadda-mysql:/var/lib/mysql
    networks:
      - deploy

networks:
  deploy:
    external: true

volumes:
  amadda-mysql:
```

## Redis
docker-compose.yml
```
version: '3'
services:
  redis:
    image: redis
    container_name: amadda-redis
    ports:
      - "6379:6379"
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./amadda-redis/data/:/data
      - ./amadda-redis/redis/redis.conf:/usr/local/etc/redis/redis.conf
    restart: always
    networks:
      - deploy

networks:
  deploy:
    external: true

volumes:
  amadda-redis:
```

## Kafka
```
version: '3.9'
services:
  zk1:
    container_name: zookeeper1
    image: wurstmeister/zookeeper:latest
    restart: always
    hostname: zk1
    ports:
      - "2181:2181"
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      TZ: Asia/Seoul
    volumes:
      - "~/zk-cluster/zk1/data:/data"
    networks:
      - deploy

  zk2:
    container_name: zookeeper2
    image: wurstmeister/zookeeper:latest
    restart: always
    hostname: zk2
    ports:
      - "2182:2181"
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      TZ: Asia/Seoul
    volumes:
      - "~/zk-cluster/zk2/data:/data"
    networks:
      - deploy

  zk3:
    container_name: zookeeper3
    image: wurstmeister/zookeeper:latest
    restart: always
    hostname: zk3
    ports:
      - "2183:2181"
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      TZ: Asia/Seoul
    volumes:
      - "~/zk-cluster/zk3/data:/data"
    networks:
      - deploy

  kafka1:
    container_name: kafka1
    image: wurstmeister/kafka:latest
    restart: on-failure
    depends_on:
      - zk1
      - zk2
      - zk3
    ports:
      - "4092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://amadda.co.kr:4092
      KAFKA_LISTENERS: PLAINTEXT://:9092      
      BOOTSTRAP_SERVERS: kafka1:9092,kafka2:9092,kafka3:9092
      KAFKA_ZOOKEEPER_CONNECT: "zk1:2181,zk2:2182,zk3:2183"
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 2
      TZ: Asia/Seoul
    networks:
      - deploy

  kafka2:
    container_name: kafka2
    image: wurstmeister/kafka:latest
    restart: on-failure
    depends_on:
      - zk1
      - zk2
      - zk3
    ports:
      - "4093:9092"
    volumes:
        - /var/run/docker.sock:/var/run/docker.sock
    environment:
      KAFKA_LISTENERS: PLAINTEXT://:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://amadda.co.kr:4093
      KAFKA_BROKER_ID: 2
      BOOTSTRAP_SERVERS: kafka1:9092,kafka2:9092,kafka3:9092
      KAFKA_ZOOKEEPER_CONNECT: "zk1:2181,zk2:2182,zk3:2183"
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 2
      TZ: Asia/Seoul
    networks:
      - deploy

  kafka3:
    container_name: kafka3
    image: wurstmeister/kafka:latest
    restart: on-failure
    depends_on:
      - zk1
      - zk2
      - zk3
    ports:
      - "4094:9092"
    volumes:
        - /var/run/docker.sock:/var/run/docker.sock
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://amadda.co.kr:4094
      KAFKA_LISTENERS: PLAINTEXT://:9092
      BOOTSTRAP_SERVERS: kafka1:9092,kafka2:9092,kafka3:9092
      KAFKA_ZOOKEEPER_CONNECT: "zk1:2181,zk2:2182,zk3:2183"
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 2
      TZ: Asia/Seoul
    networks:
      - deploy

  kafka-ui:
    container_name: kafka-ui
    image: provectuslabs/kafka-ui:latest
    restart: always
    ports:
      - "10000:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: "clout"
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: "kafka1:9092,kafka2:9092,kafka3:9092"
      KAFKA_CLUSTERS_0_ZOOKEEPER: "zk1:2181,zk2:2182,zk3:2183"
      AUTH_TYPE: "LOGIN_FORM"
      SPRING_SECURITY_USER_NAME: 
      SPRING_SECURITY_USER_PASSWORD:
      TZ: Asia/Seoul
    networks:
      - deploy

networks:
  deploy:
    external: true
```