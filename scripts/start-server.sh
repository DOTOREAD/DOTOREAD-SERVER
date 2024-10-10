#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop dotoread || true
docker rm dotoread || true
docker pull 474668395121.dkr.ecr.ap-northeast-2.amazonaws.com/dotoread:latest
docker run -d --name instagram-server -p 8080:8080 474668395121.dkr.ecr.ap-northeast-2.amazonaws.com/dotoread:latest
echo "--------------- 서버 배포 끝 -----------------"