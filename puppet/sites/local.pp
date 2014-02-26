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

# Java

class {'s3cmd':
  aws_access_key => 'AKIAIFUYOEB5F5OXIWOA',
  aws_secret_key => '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',
  gpg_passphrase => 'none',
  owner => 'root',
}
->
s3cmd::commands::get { '/opt/jdk.rpm':
  s3_object => 's3://download.corp.robonews.io/java/jdk-7u51-linux-x64.rpm',
  cwd => '/opt',
  owner => 'root'
}
->
package { 'jdk':
  provider => rpm,
  ensure => installed,
  source => '/opt/jdk.rpm'
}
# ensure JAVA_HOME is set (/etc/profile.d/java.sh)?
