#!/usr/bin/env sh

if [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
    echo "Building pull request - skip e2e test?"
#    ./gradlew clean build -x integrationTest
    ./gradlew build
else
    echo "building branch $TRAVIS_BRANCH"
    ./gradlew build
fi
