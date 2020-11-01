#!/usr/bin/perl -w

use feature qw(say);
use strict;
use warnings;
use IO::Socket;
use utf8;

my $name = "/var/folders/zd/jq2vr_t53hldzjrrz3g6ycc40000gn/T/JAVA_UNIX_DOMAIN.sock";
my $s;
socket($s, AF_UNIX, SOCK_STREAM, 0)  || die "socket: $!";
connect($s, pack_sockaddr_un($name)) || die "connect: $!";;
print $s "Hello\n";
print <$s>;
close($s);

exit;
1