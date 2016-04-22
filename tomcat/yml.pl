#!/usr/bin/perl

use strict;
use YAML qw(LoadFile);

my $num_args = $#ARGV + 1;
if ($num_args != 2) {
    print "\nUsage: yml.pl file.yml property_name\n";
    exit;
}

my $settings = LoadFile($ARGV[0]);
my $path = $ARGV[1];
my $exp = join('->',  map {'{'. $_.'}'} split('\.', $path));
print eval "\$settings->$exp";