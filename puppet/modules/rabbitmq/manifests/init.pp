
class rabbitmq {

  $version = "3.3.5"
  $url = "http://www.rabbitmq.com/releases/rabbitmq-server/v${version}/rabbitmq-server-${version}-1.noarch.rpm"

  $admin_username = "admin"
  $admin_password = "UFa0Zy8L"

  package{"erlang":
    ensure => "present",
  }

  package{"rabbitmq-server":
    provider => "rpm",
    source   => $url,
    require  => Package["erlang"],
    notify   => Exec['rabbitmq-enable-management'],
  }

  exec { 'rabbitmq-enable-management':
    command     => '/usr/lib/rabbitmq/bin/rabbitmq-plugins enable rabbitmq_management',
    environment => 'HOME=/root',
    refreshonly => true,
    require     => Package['rabbitmq-server']
  }

  service { "rabbitmq-server":
    ensure  => 'running',
    enable  => true,
    require => Package['rabbitmq-server'],
  }

  exec { 'rabbitmq-delete-guest':
    command => 'rabbitmqctl delete_user guest',
    onlyif  => 'rabbitmqctl list_users | grep -cq "^guest[[:space:]]"',
    require => Service['rabbitmq-server']
  }

  exec { 'rabbitmq-add-admin-user':
    command => "rabbitmqctl add_user ${admin_username} ${admin_password}; rabbitmqctl set_user_tags ${admin_username} administrator; rabbitmqctl set_permissions -p / ${admin_username} \".*\" \".*\" \".*\"",
    unless  => "rabbitmqctl list_users | grep -cq \"^${admin_username}[[:space:]]\"",
    require => Service['rabbitmq-server']
  }

}