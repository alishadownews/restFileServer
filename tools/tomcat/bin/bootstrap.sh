#!/bin/sh 
echo ${task_name}
echo ${archive_name}
echo "PUMP: ${PUMP}"
echo "ERZL: ${ERZL}"


if [ $DOWNLOAD_ERZL = "True" ] ; then (
  echo "Downloading erzl libs"
  cd webapps
    sleep 10
    wget -q $JENKINS_LOCATION/job/client_generic_develop_erzl/ws/erzl/target/erzl.war &
    wget -q $JENKINS_LOCATION/job/client_generic_develop_erzl/ws/erzl-admin/target/erzl-admin.war &
  cd ..
) &
fi

if [ $DOWNLOAD_PUMP = "True" ] ; then (
  echo "downloading pump libs"
  cd webapps
    sleep 10
    wget -q $JENKINS_LOCATION/job/client_generic_develop_pump/ws/PmpClient/target/PmpClient.war &
    wget -q $JENKINS_LOCATION/job/client_generic_develop_pump/ws/admin-panel-client/target/admin-panel-client.war &
  cd ..
) &
fi 

if [ $DOWNLOAD_ERZLWS = "True" ] ; then (
  echo "downloading ERZLWS lib"
  cd webapps
    sleep 4
    wget -q $JENKINS_LOCATION/job/erzl_ws/ws/target/erzlws.war &
  cd ..
) &
fi 

if [  $DOWNLOAD_SERVER = "True" ] ; then
 (
    echo "Downloading server libs"
    sleep 40
    wget -q $JENKINS_LOCATION/job/${task_name}/ws/pmp/build/target/${archive_name}.zip
    unzip ${archive_name}.zip
    rm -rf ${archive_name}.zip
    mkdir -p /var/lib/pmp/reports
    mv reports/* /var/lib/pmp/reports && mv etc/pmp /etc/ && mv modules/* webapps/ 
    rm -rf modules/ docs/ module-nsi-dist.zip
  ) &
fi

cp -r /manager /usr/local/tomcat/webapps/
chmod -R 777 /usr/local/tomcat/webapps/manager
mkdir -p /usr/local/tomcat/modules/logs
mkdir -p /usr/local/tomcat/modules/dbf/outbound
mkdir -p /usr/local/tomcat/modules/images
catalina.sh jpda run

