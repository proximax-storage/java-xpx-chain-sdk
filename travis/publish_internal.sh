#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "[NOT IMPLEMENTED] publishing to internal nexus"
fi
