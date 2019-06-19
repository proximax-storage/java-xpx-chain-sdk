#!/usr/bin/env sh

echo "publishing javadoc"

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "[NOT IMPLEMENTED] publishing javadoc"
fi
