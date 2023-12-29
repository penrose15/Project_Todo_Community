!/bin/bash
echo "> 현재 구동중인 profile 확인"
CURRENT_PROFILE=$(curl -s http://localhost/profile)

echo "> $CURRENT_PROFILE"

if [ $CURRENT_PROFILE == set1 ]
then
        IDLE_PROFILE=set2
        IDLE_PORT=8081
elif [ $CURRENT_PROFILE == set2 ]
then
        IDLE_PROFILE=set1
        IDLE_PORT=8082
else
        echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
        echo "> set1을 할당합니다. IDLE_PROFILE: set1"
        IDLE_PROFILE=set1
        IDLE_PORT=8081
fi

CONTAINER_ID=$(docker container ls -f "name=${IDLE_PROFILE}" -q)

sudo docker stop ${IDLE_PROFILE}
sudo docker rm ${IDLE_PROFILE}

TAG_ID=$(docker images | sort -r -k2  -h | awk 'NR > 1 {if ($1 == "admin1125/hsj") {print $2 += .01; exit} else {print 0.01; exit}}')

echo "> 도커 build 실행 : docker build --build-arg DEPENDENCY=build/dependency --build-arg IDLE_PROFILE=${IDLE_PROFILE} -t admin1125/hsj:${TAG_ID} ."
sudo docker build --build-arg DEPENDENCY=build/dependency --build-arg IDLE_PROFILE=${IDLE_PROFILE} -t admin1125/hsj:${TAG_ID} .


echo "> $IDLE_PROFILE 배포"

sudo docker login -u admin1125 -p rnrzktmxps15!

sudo docker push admin1125/hsj:${TAG_ID}

##tag가 latest인 image를 최신 버전을 통해 생성
sudo docker tag admin1125/hsj:${TAG_ID} admin1125/hsj:latest

##latest를 docker hub에 push
sudo docker push admin1125/hsj:latest

echo "> 도커 run 실행 :  sudo docker run --name $IDLE_PROFILE -d --rm -p $IDLE_PORT:${IDLE_PORT} admin1125/hsj  "
sudo docker run --name $IDLE_PROFILE -d --rm -p $IDLE_PORT:${IDLE_PORT} admin1125/hsj

echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10

for retry_count in {1..10}
do
        response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
        up_count=$(echo $response | grep 'UP' | wc -l)

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
sleep 10

sudo sh /home/ec2-user/switch.sh
