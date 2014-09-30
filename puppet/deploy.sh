#! /bin/sh

#puppet apply --detailed-exitcodes --modulepath=modules sites/local.pp                --environment test
puppet apply --detailed-exitcodes --modulepath=modules --hiera_config=hiera/hiera.yaml --environment dev  sites/dispatch.pp