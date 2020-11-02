#!/usr/bin/perl -w

use strict;
use warnings;
use IO::Socket::UNIX;
use utf8;

my $socket_path = "/tmp/JAVA_UNIX_DOMAIN.sock";
my $socket      = IO::Socket::UNIX->new(
    Type        => SOCK_STREAM(),
    Peer        => $socket_path
) || die("Can't connect to server: $!\n");

print $socket "foo bar\n";
my $response    = <$socket>;
close($socket);

chomp($response);
print qq{server say $response\n};

1
