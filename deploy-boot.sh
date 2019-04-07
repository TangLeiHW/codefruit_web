#!/usr/bin/env bash
#����+�������

#��Ҫ�������²���
# ��Ŀ·��, ��Execute Shell��������Ŀ·��, pwd �Ϳ��Ի�ø���Ŀ·��
# export PROJ_PATH=���jenkins�����ڲ�������ϵ�·��


### base ����
killBoot()
{
  pid=`ps -ef|grep code|grep java|awk '{print $2}'`
  echo "codefruit pid :$pid"
  if [ "$pid" = "" ]
  then
    echo "no tomcat pid alive"
  else
    kill -9 $pid
  fi
}

cd $PROJ_PATH/codefruit
mvn -Dmaven.test.skip -U clean package

#ֹͣBoot
killBoot

#������Ŀ
nohup java -jar target/code_fruit_web-0.0.1-SNAPSHOT.jar &