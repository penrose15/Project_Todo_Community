sudo docker ps -a -q --filter "name=hsj" | grep -q . && docker stop hsj && docker rm hsj | true

sudo docker ps -a -q --filter "name=myredis" | grep -q . && docker stop myredis && docker rm myredis | true

# 기존 이미지 삭제
sudo docker rmi admin1125/hsj:1.0

# 도커허브 이미지 pull
sudo docker pull admin1125/hsj:1.0

# 도커 run
docker run -d -p 8080:8080 -v /home/ec2-user:/config --name hsj admin1125/hsj:1.0
# redis start
docker run -d --name myredis -p 6379:6379 redis

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
docker rmi -f $(docker images -f "dangling=true" -q) || true