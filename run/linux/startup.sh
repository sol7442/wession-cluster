###########################
###########################


###########################
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_RUN=${JAVA_HOME}/bin/java
###########################


APP_HOME=''
CLASSPATH='';
###########################
APP_PACKAGE='ws-scim-im'
MAIN_CLASS='com.wowsanta.daemon.WowsataDaemon';
PID_FILE='scim.pid'
VM_OPTION='-Xms64m -Xmx512m'
LOG_MODE='INFO'
###########################
APP_OUT=''
APP_ERR=''
APP_RUN_MODE='DEBUG'

######################################################
find_apphome(){ 
cd ..;
APP_HOME=${PWD}
cd bin;
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
CLASSPATH="${DISTPATH}:${CLASSPATH}"

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
    -Dapp.name=${APP_PACKAGE} \
    -Dconfig.file=${CONF_PATH}/daemon_config.json \
    -Dlogback.path=${LOG_PATH} \
    -Dlogback.mode=${LOG_MODE} \
    -Dlogback.configurationFile=${LOG_CONF} \
    ${MAIN_CLASS} &

echo "$!" > ${PID_FILE}
