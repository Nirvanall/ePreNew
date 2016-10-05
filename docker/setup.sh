#!/bin/sh
# This script requires root priviledge

NGINX=nginx-epre
MYSQL=mysql-youcai
REDIS=redis-youcai
JDK=jdk-youcai

TIMEZONE='TZ=Asia/Shanghai'
SETXTERM='TERM=xterm'
BASEDIR=/data/epre
WEBDIR=$BASEDIR/ePreNew

set -e
set -x
add-apt-repository ppa:saiarcot895/myppa
apt-get update
apt-get -y install apt-fast
curl -sSL https://git.daocloud.io/docker | sed 's/apt-get install/apt-fast install/g' | sh

[ -d $BASEDIR/jdk ] || mkdir $BASEDIR/jdk -p
echo '#!/bin/bash
while ;
    sleep 1
endw' > $BASEDIR/jdk/sleep.sh
apt-fast install -y zip
curl -o $BASEDIR/jdk/gradle-3.1-bin.zip https://services.gradle.org/distributions/gradle-3.1-bin.zip
unzip $BASEDIR/jdk/gradle-3.1-bin.zip

[ -d $BASEDIR/conf/redis ] || mkdir $BASEDIR/conf/redis -p
[ -e $BASEDIR/conf/redis/redis.conf ] || curl -o $BASEDIR/conf/redis/redis.conf https://raw.githubusercontent.com/antirez/redis/3.2/redis.conf

docker rm -fv $NGINX $JDK $MYSQL $REDIS || echo ''

docker run --name $REDIS \
-v $BASEDIR/redis:/home \
-v $BASEDIR/data/redis:/data \
-v $BASEDIR/conf/redis:/usr/local/etc/redis \
-v $BASEDIR/log/redis:/var/log/redis \
-w /home \
-e $TIMEZONE \
-e $SETXTERM \
-d daocloud.io/library/redis:3.2 \
redis-server /usr/local/etc/redis/redis.conf

docker run --name $MYSQL \
-v $BASEDIR/mysql:/home \
-v $BASEDIR/data/mysql:/var/lib/mysql \
-v $BASEDIR/conf/mysql:/etc/mysql/conf.d \
-v $BASEDIR/log/mysql:/var/log/mysql \
-w /home \
-e MYSQL_ROOT_PASSWORD='R00T@root!' \
-e MYSQL_DATABASE=ePre \
-e MYSQL_USER=fyp \
-e MYSQL_PASSWORD=FYP2014_fyp2016 \
-e $SETXTERM \
-e $TIMEZONE \
-d daocloud.io/library/mysql:5.7

docker run --name $JDK \
--link=$REDIS:redis \
--link=$MYSQL:mysql \
-p 8080:8080 \
-v $BASEDIR/jdk:/home \
-v $WEBDIR:/data/web \
-w /data/web \
-e $SETXTERM \
-e $TIMEZONE \
-e GRADLE_HOME=/home/gradle-3.1-bin \
-d daocloud.io/library/openjdk:8-jdk

docker run --name $NGINX \
--link=$JDK:jdk \
-p 80:80 \
-p 443:443 \
-v $BASEDIR/nginx:/home \
-v $WEBDIR:/data/web \
-v $BASEDIR/conf/nginx:/etc/nginx/conf.d \
-v $BASEDIR/log/nginx:/var/log/nginx \
-w /data/web \
-e $SETXTERM \
-e $TIMEZONE \
-d daocloud.io/library/nginx
