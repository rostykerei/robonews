#!/bin/bash
#
#       /etc/rc.d/init.d/robonews-${project.artifactId}
#
# chkconfig: 235 08 92
#

# Source function library.
. /etc/init.d/functions

worker() {
    /usr/local/robonews/${project.artifactId}/bin/${project.artifactId}.sh $1 > /dev/null
    if [ $? -eq 0 ] ; then
        echo_success
        echo
    else
        echo_failure
        echo
        exit 1
    fi
}

case "$1" in
    start)
        echo -n "Starting robonews-${project.artifactId}: "
        worker start
        ;;
    stop)
        echo -n "Stoppping robonews-${project.artifactId}: "
        worker stop
        ;;
    force-stop)
        echo -n "Stoppping (enforced) robonews-${project.artifactId}: "
        worker force-stop
        ;;
    status)
	    /bin/sh /usr/local/robonews/${project.artifactId}/bin/${project.artifactId}.sh status
        ;;
    restart)
        echo -n "Restarting robonews-${project.artifactId}: "
        worker restart
        ;;
    condrestart)
        echo -n "Try to restarting robonews-${project.artifactId}: "
        worker condrestart
        ;;
    *)
        echo "Usage: robonews-${project.artifactId} {start|stop|stop-force|restart|condrestart|status}"
        exit 1
        ;;
esac
exit $?