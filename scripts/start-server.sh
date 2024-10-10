#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop dotoread || true
docker rm dotoread || true
docker pull ${ECR_ADDRESS}/dotoread:latest
docker run -d --name dotoread -p 8080:8080 ${ECR_ADDRESS}/dotoread:latest
echo "--------------- 서버 배포 끝 -----------------"