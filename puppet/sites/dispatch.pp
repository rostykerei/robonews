  if versioncmp($::puppetversion,'3.6.1') >= 0 {
    $allow_virtual_packages = hiera('allow_virtual_packages',false)

    Package {
      allow_virtual => $allow_virtual_packages,
    }
  }

  Exec { path => [ '/bin/', '/sbin/' , '/usr/bin/', '/usr/sbin/' ] }

  hiera_include('classes')

  robonews::hiera::resources { 'hiera_resources': }