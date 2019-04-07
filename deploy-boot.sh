#!/usr/bin/env bash
#编译+部署码果

#需要配置如下参数
# 项目路径, 在Execute Shell中配置项目路径, pwd 就可以获得该项目路径
# export PROJ_PATH=这个jenkins任务在部署机器上的路径


### base 函数
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

#停止Boot
killBoot

#启动项目
nohup java -jar target/code_fruit_web-0.0.1-SNAPSHOT.jar &