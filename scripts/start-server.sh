#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop instagram-server || true
docker rm instagram-server || true
docker pull ${ECR_ADDRESS}:latest
docker run -d --name dotoread -p 8080:8080 ${ECR_ADDRESS}:latest
echo "--------------- 서버 배포 끝 -----------------"