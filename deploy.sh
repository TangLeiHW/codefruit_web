#!/usr/bin/env bash
#����+����orderվ��

#��Ҫ�������²���
# ��Ŀ·��, ��Execute Shell��������Ŀ·��, pwd �Ϳ��Ի�ø���Ŀ·��
# export PROJ_PATH=���jenkins�����ڲ�������ϵ�·��

# ������Ļ�����tomcat��ȫ·��
# export TOMCAT_APP_PATH=tomcat�ڲ�������ϵ�·��

### base ����
killTomcat()
{
    pid=`ps -ef|grep tomcat|grep java|awk '{print $2}'`
    echo "tomcat Id list :$pid"
    if [ "$pid" = "" ]
    then
      echo "no tomcat pid alive"
    else
      kill -9 $pid
    fi
}
cd $PROJ_PATH/order
mvn clean install

# ͣtomcat
killTomcat

# ɾ��ԭ�й���
rm -rf $TOMCAT_APP_PATH/webapps/ROOT
rm -f $TOMCAT_APP_PATH/webapps/ROOT.war
rm -f $TOMCAT_APP_PATH/webapps/order.war

# �����µĹ���
cp $PROJ_PATH/order/target/order.war $TOMCAT_APP_PATH/webapps/

cd $TOMCAT_APP_PATH/webapps/
mv order.war ROOT.war

# ����Tomcat
cd $TOMCAT_APP_PATH/
sh bin/startup.sh