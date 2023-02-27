#!/bin/bash

echo "> 현재 구동중인 profile 확인"
CURRENT_PROFILE=$(curl -s http://127.0.0.1/profile)
echo "> $CURRENT_PROFILE"

if [ $CURRENT_PROFILE == prod1 ]
then
  IDLE_PROFILE=prod2
  IDLE_PORT=8081
elif [ $CURRENT_PROFILE == prod2 ]
then
  IDLE_PROFILE=prod1
  IDLE_PORT=8080
else
  echo "> 일치하는 프로필이 없습니다. PROFILE : $CURRENT_PROFILE"
  echo "> prod1을 할당합니다. IDLE_PROFILE: prod1"
  IDLE_PROFILE=prod1
  IDLE_PORT=8080
fi

sudo docker ps -a -q --filter "name=hsj" | grep -q . && docker stop hsj && docker rm hsj | true

sudo docker rmi admin1125/hsj:1

ADMIN=admin1125

IMAGE_NAME=hsj
TAG_ID=$(docker images | sort -r -k2 -h | grep "${IMAGE_NAME}" | awk 'BEGIN{tag = 1} NR==1{tag += $2} END{print tag}')

sudo chmod 755 .test.sh.swp


echo "> 도커 pull"
sudo docker pull admin1125/hsj:1

echo "> $IDLE_PROFILE 배포"
echo "> 도커 run 실행 :  sudo docker run --name ${IDLE_PROFILE} -d --rm -p $IDLE_PORT:${IDLE_PORT} ${ADMIN}/${IMAGE_NAME}:${TAG_ID}"
sudo docker run -e "SPRING_PROFILES_ACTIVE=prod1" --name ${IDLE_PROFILE} -d --rm -p $IDLE_PORT:${IDLE_PORT} ${ADMIN}/${IMAGE_NAME}:${TAG_ID}


# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
docker rmi -f $(docker images -f "dangling=true" -q) || true

echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10



for retry_count in {1..10}
do
  response=$(curl -s http://127.0.0.1:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  echo "> this is ${response}"

  if [ $up_count -ge 1 ]
  then
    echo "> Health check 성공"
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
    echo "> Health check: ${response}"
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

echo "> 스위칭을 시도합니다..."
sleep 5

echo "> 현재 구동중인 Port 확인"
CURRENT_PROFILE=$(curl -s http://localhost/profile)

if [ $CURRENT_PROFILE == prod1 ]
then
  CURRENT_PORT=8080
  IDLE_PORT=8081
elif [ $CURRENT_PROFILE == prod2 ]
then
  CURRENT_PORT=8081
  IDLE_PORT=8080
else
  echo "> 일치하는 Profile이 없습니다. Profile:$CURRENT_PROFILE"
  echo "> 9001을 할당합니다."
  IDLE_PORT=8080
fi

echo "> 현재 구동중인 Port: $CURRENT_PORT"
echo "> 전환할 Port : $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

echo "> ${CURRENT_PROFILE} 컨테이너 삭제"
sudo docker stop $CURRENT_PROFILE
sudo docker rm $CURRENT_PROFILE

echo "> Nginx Reload"

sudo service nginx reload