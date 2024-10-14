#!/bin/bash
echo "--------------- 서버 배포 시작 -----------------"

# ECR 로그인
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 474668395121.dkr.ecr.ap-northeast-2.amazonaws.com

# 기존 컨테이너 중지 및 제거
docker-compose down

# 최신 이미지 풀
docker pull 474668395121.dkr.ecr.ap-northeast-2.amazonaws.com/dotoread:latest

# 컨테이너 시작
docker-compose up -d

echo "--------------- 서버 배포 끝 -----------------"