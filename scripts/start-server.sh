#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop dotoread || true
docker rm dotoread || true
docker pull ${ECR_ADDRESS}:latest
docker run -d --name instagram-server -p 8080:8080 ${ECR_ADDRESS}:latest
echo "--------------- 서버 배포 끝 -----------------"