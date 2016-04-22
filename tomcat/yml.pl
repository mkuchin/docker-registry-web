#!/usr/bin/perl

use strict;
use YAML qw(Load);
use File::Slurp;

my $num_args = $#ARGV + 1;
if ($num_args != 2) {
    print "\nUsage: yml.pl file.yml property_name\n";
    exit;
}
my $text = read_file($ARGV[0])."\n";
my $settings = Load($text);
my $path = $ARGV[1];
my $exp = join '->',  map {'{'. $_.'}'} split '\.', $path;
print eval "\$settings->$exp";