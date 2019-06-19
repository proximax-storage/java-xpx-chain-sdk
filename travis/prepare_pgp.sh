#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "[NOT IMPLEMENTED] prepare gpg key"
#    openssl aes-256-cbc -K $encrypted_XXXXXX_key -iv $encrypted_XXXXXX_iv -in $ENCRYPTED_GPG_KEY_LOCATION -out $GPG_KEY_LOCATION -d
fi