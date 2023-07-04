#!/bin/bash

REPOSITORY=/home/ubuntu/app
PROJECT_NAME=backend
DEPLOY_LOG=/home/ubuntu/app/deploy.log

echo "> Build 파일 복사" >> $DEPLOY_LOG

cp $REPOSITORY/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 PID 확인" >> $DEPLOY_LOG
CURRENT_PID=$(pgrep -f backend)

echo "> 현재 구동중인 애플리케이션 PID: $CURRENT_PID" >> $DEPLOY_LOG

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없어 종료하지 않습니다." >> $DEPLOY_LOG
else
  echo "> kill -15 $CURRENT_PID" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새로운 애플리케이션 배포" >> $DEPLOY_LOG

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR 이름: $JAR_NAME" >> $DEPLOY_LOG
echo "> $JAR_NAME 에 실행권한 추가" >> $DEPLOY_LOG
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행" >> $DEPLOY_LOG

nohup java -jar -DSpring.config.location=classpath:/application.yml -Dspring.profiles.active=prod $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

echo "배포 완료!" >> $DEPLOY_LOG
