# Time and date settings

class { 'ntp':
    servers => [
        '0.pool.ntp.org',
        'time.nist.gov',
        '1.pool.ntp.org'
    ]
}

class { 'timezone':
    timezone => 'UTC'
}

# Basic packages

package { 'wget':
  ensure => 'installed'
}

package { 'vim-minimal':
  ensure => 'installed'
}

package { 'mc':
  ensure => 'installed'
}

#Java

exec { 'download_jdk':
    command => '/usr/bin/wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com" "http://download.oracle.com/otn-pub/java/jdk/6u45-b06/jdk-6u45-linux-x64-rpm.bin" -O /opt/jdk.bin',
    creates => '/opt/jdk.bin',
    require => Package['wget']
}

file { '/opt/jdk.bin':
    ensure  => 'present',
    mode    => '0755',
    require => Exec['download_jdk']
}

exec { 'jdk-install':
    command => '/opt/jdk.bin',
    cwd => '/tmp',
    require => File['/opt/jdk.bin']
}

exec { 'delete-temp-rpms':
    command => '/bin/rm -f /tmp/sun-*.rpm /tmp/jdk-*.rpm',
    require => Exec['jdk-install']
}