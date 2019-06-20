#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "publishing snapshot to public repository"
	./gradlew publishAllPublicationsToSnapshotRepository
fi

if [ -n "$TRAVIS_TAG" ];
then 
	echo "publishing release $TRAVIS_TAG to public repository"
	./gradlew publishAllPublicationsToReleaseRepository -Pversion=$TRAVIS_TAG
fi