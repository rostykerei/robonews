define robonews::hiera::resources {
  $resources = hiera_array("resources", undef)

  if $resources {
    robonews::hiera::create_resource { $resources: }
  }
}