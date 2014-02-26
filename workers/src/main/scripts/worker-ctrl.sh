#!/bin/sh
# Robonews.io
#
# Copyright (c) 2013-2014 Rosty Kerei.
# All rights reserved.

LAUNCHER_CLASS="${launcher.class}"

WORKER_NAME="${project.artifactId}"
VERSION="${project.version}"
BUILD_DATE="${timestamp}"

JAVA_OPTS=""

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`/..
# make it fully qualified
PRGDIR=`cd "$PRGDIR" && pwd`

PID_FILE=/var/run/robonews/"$WORKER_NAME".pid
CONFIG_FILE=/etc/robonews/"$WORKER_NAME"/"$WORKER_NAME".properties
LOG4J_CONFIG=/etc/robonews/"$WORKER_NAME"/log4j.properties
LOG_FILE=/var/log/robonews/"$WORKER_NAME".out
JAVA_PROG=`which java`

WORKER_PID=0

update_worker_pid() {
    if [ -r "$PID_FILE" ]; then
        PID=`cat "$PID_FILE"`
        ps -p "$PID" > /dev/null 2>&1
        if [ $? -eq 0 ] ; then
            WORKER_PID="$PID"
        else
            rm -f "$PID_FILE" > /dev/null 2>&1
            if [ $? != 0 ]; then
                if [ -w "$PID_FILE" ]; then
                    cat /dev/null > "$PID_FILE"
                else
                    echo "Unable to remove or clear stale PID file." >&2
                    exit 1
                fi
            fi
            WORKER_PID=0
        fi
    else
        WORKER_PID=0
    fi
}

start() {
    update_worker_pid

    if [ "$WORKER_PID" -ne 0 ] ; then
        echo "Process $WORKER_NAME appears to still be running with PID $WORKER_PID. Start aborted." >&2
        exit 1
    fi

    echo /dev/null > "$PID_FILE" 2>&1

    if [ $? -ne 0 ]; then
        echo "Unable to write to PID file. Start aborted." >&2
        exit 1
    fi

    CLASSPATH="$PRGDIR/lib/*"

    echo "Starting $WORKER_NAME...."
    echo "Java in use:      $JAVA_PROG"
    echo "Launcher class:   $LAUNCHER_CLASS"
    echo "Config file:      $CONFIG_FILE";
    echo "Output log:       $LOG_FILE";
    echo "PID file:         $PID_FILE"

    touch "$LOG_FILE"

    WORKER_EXEC='java '$JAVA_OPTS'-Dlog4j.configuration=file:'$LOG4J_CONFIG' -classpath '\""$CLASSPATH"\"' -Dconfig.file='$CONFIG_FILE' '"$LAUNCHER_CLASS"

    echo "$WORKER_EXEC" >> "$LOG_FILE"

    eval "$WORKER_EXEC" >> "$LOG_FILE" 2>&1 &

    echo $! > "$PID_FILE"

    echo "PID:              $!"

    return 0
}

stop() {
    echo "Stopping $WORKER_NAME"

    if [ -r "$PID_FILE" ]; then
        PID=`cat "$PID_FILE"`

        if [ "$1" = "force" ]; then
            kill -9 $PID >/dev/null 2>&1
        else
            kill -1 $PID >/dev/null 2>&1
        fi

        KILL_SLEEP_INTERVAL=5

        while [ $KILL_SLEEP_INTERVAL -ge 0 ]; do
            ps -p $PID >/dev/null 2>&1
            if [ $? -eq 0 ] ; then
                if [ $KILL_SLEEP_INTERVAL -gt 0 ]; then
                    sleep 1
                fi

                KILL_SLEEP_INTERVAL=`expr $KILL_SLEEP_INTERVAL - 1`
            else
                rm -f "$PID_FILE" > /dev/null 2>&1
                KILL_SLEEP_INTERVAL=0
                echo "Process $WORKER_NAME stopped"
                return 0
            fi
        done

        if [ $KILL_SLEEP_INTERVAL -ne 0 ]; then
            echo "Process $WORKER_NAME has not been killed completely yet." >&2
        fi
    else
        echo "Unable to read PID file. Process $WORKER_NAME already stopped?" >&2
    fi

    exit 1
}

status() {
    update_worker_pid
    if [ "$WORKER_PID" -ne 0 ] ; then
        echo "Process $WORKER_NAME running, PID $WORKER_PID"
    else
        echo "Process $WORKER_NAME stopped"
    fi

    return 0
}

restart() {
    update_worker_pid
    if [ "$WORKER_PID" -ne 0 ] ; then
        stop
    fi

    start
}

condrestart() {
    update_worker_pid
    if [ "$WORKER_PID" -ne 0 ] ; then
        stop
        start
    fi
}

version() {
    echo $WORKER_NAME
    echo "Version:     $VERSION"
    echo "Build Date:  $BUILD_DATE"
    echo
    return 0
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    force-stop)
        stop "force"
        ;;
    restart)
        restart
        ;;
    condrestart)
        condrestart
        ;;
    status)
        status
        ;;
    version)
        version
        ;;
    *)
        echo "Usage: $WORKER_NAME.sh {start|stop|force-stop|restart|condrestart|status|version}"
        exit 1
        ;;
esac
exit $?

