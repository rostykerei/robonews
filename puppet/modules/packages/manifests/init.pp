class packages {
  $packages = hiera_array('packages')

  package { $packages:
    ensure => 'latest',
    provider => 'yum'
  }
}