#!/bin/bash
#
#       /etc/rc.d/init.d/robonews-${project.artifactId}
#
# chkconfig: 2345 90 01
#

# Source function library.
. /etc/init.d/functions

worker() {
    su robonews /usr/local/robonews/${project.artifactId}/bin/${project.artifactId}.sh $1 > /dev/null
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
        touch /var/lock/subsys/robonews-${project.artifactId}
        ;;
    stop)
        echo -n "Stopping robonews-${project.artifactId}: "
        worker stop
        rm -f /var/lock/subsys/robonews-${project.artifactId}
        ;;
    force-stop)
        echo -n "Stoppping (enforced) robonews-${project.artifactId}: "
        worker force-stop
        rm -f /var/lock/subsys/robonews-${project.artifactId}
        ;;
    condstop)
        echo -n "Trying to stop robonews-${project.artifactId}: "
        worker condstop
        rm -f /var/lock/subsys/robonews-${project.artifactId}
        ;;
    status)
	    su robonews /usr/local/robonews/${project.artifactId}/bin/${project.artifactId}.sh status
        ;;
    restart)
        echo -n "Restarting robonews-${project.artifactId}: "
        worker restart
        touch /var/lock/subsys/robonews-${project.artifactId}
        ;;
    condrestart)
        echo -n "Try to restarting robonews-${project.artifactId}: "
        worker condrestart
        ;;
    *)
        echo "Usage: robonews-${project.artifactId} {start|stop|stop-force|condstop|restart|condrestart|status}"
        exit 1
        ;;
esac
exit $?