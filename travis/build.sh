#!/usr/bin/env sh

if [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
    echo "Building pull request - skip e2e test?"
#    ./gradlew clean build -x integrationTest
    ./gradlew clean build
else
    echo "building branch $TRAVIS_BRANCH"
    ./gradlew clean build
fi
