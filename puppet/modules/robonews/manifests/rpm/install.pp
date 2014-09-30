define robonews::rpm::install ($rpm) {

  require s3cmd

  file { "/var/downloads":
    ensure => "directory",
  }
  ->
  file { "/var/downloads/RPMs":
    ensure => "directory",
  }
  ->
  s3cmd::commands::get { "/var/downloads/RPMs/${rpm}":
    s3_object => "s3://download.corp.robonews.io/RPMs/${rpm}",
    cwd => '/var/downloads/RPMs',
    owner => 'root'
  }
  ->
  package { "${name}":
    provider => rpm,
    ensure => installed,
    source => "/var/downloads/RPMs/${rpm}"
  }

}