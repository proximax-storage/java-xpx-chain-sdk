#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "publishing release to public repositories"
#	./gradlew publishAllPublicationsToMavenCentralRepository
fi
