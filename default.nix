{ pkgs ? import <nixpkgs> {} }:

let
  name = "jira-tempo-begone-shell";
in
pkgs.mkShell {
  inherit name;

  shellHook = ''
    echo 'Welcome to ${name}!'
  '';

  buildInputs = with pkgs; [
    cacert
    coreutils-full
    curlFull
    glibcLocales 
    jdk
    sbt
    busybox # always last
  ];
}
