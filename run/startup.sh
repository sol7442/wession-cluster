###########################
###########################


###########################
JAVA_HOME=/usr/local/jdk1.8.0_221
JAVA_RUN=${JAVA_HOME}/bin/java
###########################


APP_HOME=''
CLASSPATH='';
###########################
APP_PACKAGE='ws-wession'
MAIN_CLASS='com.wowsanta.daemon.WowSantaDaemon';
SERVICE_CLASS='com.wowsanta.wession.impl.Wession';
SERVICE_CONFIG='../config/nio.server.json';
PID_FILE='wow-daemon.pid'

VM_OPTION='-Xms64m -Xmx512m'
LOG_MODE='DEBUG'
###########################
APP_OUT=''
APP_ERR=''
APP_RUN_MODE='DEBUG'

######################################################
find_apphome(){ 
cd ..;
APP_HOME=${PWD}
cd run;
}

find_classpath(){ 
    for i in `ls ${APP_HOME}/libs/*.jar`
    do
        CLASSPATH=${CLASSPATH}:${i}
    done
}

find_distpath(){ 
    for i in `ls ${APP_HOME}/dist/*.jar`
    do
        DISTPATH=${DISTPATH}:${i}
    done
}
######################################################

find_apphome;
find_classpath;
find_distpath;


LOG_PATH="${APP_HOME}/logs"
CONF_PATH="${APP_HOME}/config"
LOG_CONF="${CONF_PATH}/logback.xml"
CLASSPATH="${DISTPATH}:${CLASSPATH}:${APP_HOME}/bin"

######################################################

echo "HOME=${HOME}"
echo "JAVA_HOME=${JAVA_HOME}"
echo "APP_HOME=${APP_HOME}"
echo "APP_PACKAGE=${APP_PACKAGE}"
echo "MAIN_CLASS=${MAIN_CLASS}"
echo "CLASSPATH=${CLASSPATH}"
echo "LOG_PATH=${LOG_PATH}"
echo "CONF_PATH=${CONF_PATH}"

######################################################

###########################
###########################
echo ${PWD}

nohup ${JAVA_RUN} \
    -classpath ${CLASSPATH} \
    ${VM_OPTION} \
    -Dapp.name=${APP_PACKAGE} \
    -Dconfig.file=${CONF_PATH}/dev.wession.json \
    -Dlogback.path=${LOG_PATH} \
    -Dlogback.mode=${LOG_MODE} \
    -Dlogback.configurationFile=${LOG_CONF} \
    ${MAIN_CLASS} ${SERVICE_CLASS} ${SERVICE_CONFIG}&

echo "$!" > ${PID_FILE}
