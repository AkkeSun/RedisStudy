# [Redis Study]

- 해당 프로젝트는 Spring Boot + Redis 연동 테스트를 위한 프로젝트 입니다
- 프로젝트를 테스트하기 전에  Redis를 설치해야 합니다.
---

### Redis 설치 가이드

- 서버 루트 권한이 필요합니다.
- setup 폴더의 service.sh 를 사용해 redis 를 조작할 수 있습니다.

 ```
# 설정파일 수정
각각의 폴더 (master1, master2, master3, slave1, slave2, slave3) 에 있는 redis.conf 의 절대 경로 변경
 
# setup 폴더로 이동 후 service.sh 파일을 실행 가능하도록 권한 변경
sudo chmod +x service.sh 
   
# redis 설치
./service.sh install

# 클러스터링 구축
redis-cli -p 7071 --cluster create 127.0.0.1:7071 127.0.0.1:7072 127.0.0.1:7073 127.0.0.1:7074 127.0.0.1:7075 127.0.0.1:7076 --cluster-replicas 1

# redis 실행
./service.sh start

# redis 종료
./service.sh end
 ```

<br>
<br>