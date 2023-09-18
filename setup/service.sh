#!/bin/bash

case $1 in
    install)
        wget https://download.redis.io/releases/redis-6.2.6.tar.gz
        tar xzf redis-6.2.6.tar.gz
        rm redis-6.2.6.tar.gz
        mv redis-6.2.6 source
        cd source
        make
        sudo make install
    ;;
    start)
        if [ -f master1/redis.pid ]; then
            echo "redis already running";
        else
          source/src/redis-server master1/redis.conf
          source/src/redis-server master2/redis.conf
          source/src/redis-server master3/redis.conf
          source/src/redis-server slave1/redis.conf
          source/src/redis-server slave2/redis.conf
          source/src/redis-server slave3/redis.conf
          echo "redis start";
        fi
    ;;
    cluster)
        redis-cli -p 7071 --cluster create 127.0.0.1:7071 127.0.0.1:7072 127.0.0.1:7073 127.0.0.1:7074 127.0.0.1:7075 127.0.0.1:7076 --cluster-replicas 1
        echo "cluster setting success";
    ;;
    stop)
        if [ -f master1/redis.pid ]; then
            PID=$(cat master1/redis.pid);
            echo "master1 stopping ...";
            kill $PID;
            echo "master1 stopped";
            rm master1/redis.pid
        else
            echo "master1 is not running"
        fi
        if [ -f master2/redis.pid ]; then
            PID=$(cat master2/redis.pid);
            echo "master2 stopping ...";
            kill $PID;
            echo "master2 stopped";
            rm master2/redis.pid
        else
            echo "master2 is not running"
        fi
        if [ -f master3/redis.pid ]; then
            PID=$(cat master3/redis.pid);
            echo "master3 stopping ...";
            kill $PID;
            echo "master3 stopped";
            rm master3/redis.pid
        else
            echo "master3 is not running"
        fi
        if [ -f slave1/redis.pid ]; then
            PID=$(cat slave1/redis.pid);
            echo "slave1 stopping ...";
            kill $PID;
            echo "slave1 stopped";
            rm slave1/redis.pid
        else
            echo "slave1 is not running"
        fi
        if [ -f slave2/redis.pid ]; then
            PID=$(cat slave2/redis.pid);
            echo "slave2 stopping ...";
            kill $PID;
            echo "slave2 stopped";
            rm slave2/redis.pid
        else
            echo "slave2 is not running"
        fi
        if [ -f slave3/redis.pid ]; then
            PID=$(cat slave3/redis.pid);
            echo "slave3 stopping ...";
            kill $PID;
            echo "slave3 stopped";
            rm slave3/redis.pid
        else
            echo "slave3 is not running"
        fi
;;
esac