#!/bin/sh
rpm -ivh http://yum.puppetlabs.com/puppetlabs-release-el-6.noarch.rpm http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm

yum upgrade
yum install puppet sshfs

mkdir ~/puppet
sshfs rosty@rosty-desktop:/home/rosty/Dropbox/news/puppet ~/puppet -o allow_other -o uid=1000 -o gid=1000