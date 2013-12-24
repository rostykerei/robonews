#!/bin/sh

WORKER_NAME="${project.artifactId}"
VERSION="${project.version}"
BUILD_DATE="${timestamp}"

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

PID_FILE="$HOME"/.news-worker-"$WORKER_NAME".pid
LAUNCHER_CLASS=nl.rostykerei.news.worker.Launcher
CONFIG_FILE_NAME="$WORKER_NAME".conf
LOG_FILE="$PRGDIR"/logs/"$WORKER_NAME".out

if [ -z "$JAVA_HOME" ] ; then
  echo "ERROR: JAVA_HOME environment variable is not set."
  exit 1;
fi

if [ "$1" = "start" ] ; then

  have_tty=0
  if [ "`tty`" != "not a tty" ]; then
    have_tty=1
  fi

  if [ -f "$PID_FILE" ]; then
    if [ -s "$PID_FILE" ]; then
      echo "Existing PID file found during start."
      if [ -r "$PID_FILE" ]; then
        PID=`cat "$PID_FILE"`
        ps -p $PID >/dev/null 2>&1
        if [ $? -eq 0 ] ; then
          echo "Process $WORKER_NAME appears to still be running with PID $PID. Start aborted."
          exit 1
        else
          echo "Removing/clearing stale PID file."
          rm -f "$PID_FILE" >/dev/null 2>&1
          if [ $? != 0 ]; then
            if [ -w "$PID_FILE" ]; then
              cat /dev/null > "$PID_FILE"
            else
              echo "Unable to remove or clear stale PID file. Start aborted."
              exit 1
            fi
          fi
        fi
      else
        echo "Unable to read PID file. Start aborted."
        exit 1
      fi
    else
      rm -f "$PID_FILE" >/dev/null 2>&1
      if [ $? != 0 ]; then
        if [ ! -w "$PID_FILE" ]; then
          echo "Unable to remove or write to empty PID file. Start aborted."
          exit 1
        fi
      fi
    fi
  fi

  echo /dev/null > "$PID_FILE" 2>&1
  if [ $? -ne 0 ]; then
    echo "Unable to write to PID file. Start aborted."
    exit 1
  fi

  if [ $have_tty -eq 1 ]; then
    echo "Starting $WORKER_NAME...."
    echo "Using JAVA_HOME:  $JAVA_HOME"
    echo "Output log:       $LOG_FILE";
    echo "PID file:         $PID_FILE"
  fi

  touch "$LOG_FILE"

  eval "$JAVA_HOME"/bin/java "$JAVA_OPTS" \
        -Djava.ext.dirs="$PRGDIR"/lib/ \
        -Dconfig.file="$PRGDIR"/conf/"$CONFIG_FILE_NAME" \
        "$LAUNCHER_CLASS" \
        >> "$LOG_FILE" 2>&1 "&"

  echo $! > "$PID_FILE"

  if [ $have_tty -eq 1 ]; then
    echo "PID:              $!"
  fi

elif [ "$1" = "stop" ] ; then

  shift

  if [ -r "$PID_FILE" ]; then
    PID=`cat "$PID_FILE"`

    if [ "$1" = "-force" ]; then
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
        KILL_SLEEP_INTERVAL=`expr $KILL_SLEEP_INTERVAL - 1 `
      else
        rm -f "$PID_FILE" >/dev/null 2>&1
        KILL_SLEEP_INTERVAL=0
        echo "Process $WORKER_NAME stopped"
        break
      fi
    done

    if [ $KILL_SLEEP_INTERVAL -ne 0 ]; then
        echo "Process $WORKER_NAME has not been killed completely yet."
    fi
  else
    echo "Unable to read PID file. Process $WORKER_NAME already stopped?"
    exit 1
  fi
elif [ "$1" = "status" ] ; then

  if [ -r "$PID_FILE" ]; then
    PID=`cat "$PID_FILE"`
    ps -p $PID >/dev/null 2>&1

    if [ $? -eq 0 ] ; then
      echo "Process $WORKER_NAME running, PID $PID"
      exit 1
    else
      echo "Process $WORKER_NAME stopped"
      echo "Removing/clearing stale PID file."
      rm -f "$PID_FILE" >/dev/null 2>&1
      if [ $? != 0 ]; then
        if [ -w "$PID_FILE" ]; then
          cat /dev/null > "$PID_FILE"
        else
          echo "Unable to remove or clear stale PID file."
        fi
      fi
      exit 1
    fi
  fi

  echo "Process $WORKER_NAME stopped"
  exit 1
elif [ "$1" = "version" ] ; then
  echo "Name:        $WORKER_NAME"
  echo "Version:     $VERSION"
  echo "Build Date:  $BUILD_DATE"
  exit 1

else

  echo "Usage: worker-control.sh ( command )"
  echo "commands:"
  echo "  start             Start $WORKER_NAME"
  echo "  stop              Stop $WORKER_NAME"
  echo "  stop -force       Kill -9 $WORKER_NAME"
  echo "  version           Version info"
  exit 1

fi

