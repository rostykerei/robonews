#!/bin/sh
rpm -ivh https://yum.puppetlabs.com/el/6/products/x86_64/puppetlabs-release-6-7.noarch.rpm http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
yum install sshfs puppet
mkdir /root/puppet
sshfs rosty@rosty-desktop:/home/rosty/Dropbox/news/puppet /root/puppet
cd puppet/
./deploy.sh