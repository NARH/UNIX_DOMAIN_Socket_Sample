#!/usr/bin/perl -w

use feature qw(say);
use strict;
use warnings;
use IO::Socket;
use utf8; 

my $socket_file = 'mysocket';
unlink $socket_file or die "Could not delete socket file '$socket_file': $!" if ( -e $socket_file ) ;

my $sock = IO::Socket::UNIX->new(
    Local       => $socket_file,
    Type      => SOCK_STREAM,
    Listen    => 5, # listen to max 5 connections
) or die "Could not create socket: '$@'";

say "Created socket successfully..";
sleep 1;
close $sock;
say "Closed socket..";

1