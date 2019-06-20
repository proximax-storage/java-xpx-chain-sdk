#!/usr/bin/env sh

# signing key needs to be decoded from base64 to PGP armored text format
export ORG_GRADLE_PROJECT_signingKey=`base64 -d $GPG_SECRET_KEYS`
# password is used as is
export ORG_GRADLE_PROJECT_signingPassword=$GPG_PASSPHRASE

# sonatype publishing credentials
export ORG_GRADLE_PROJECT_sonatypeUsername=$SONATYPE_USERNAME
export ORG_GRADLE_PROJECT_sonatypePassword=$SONATYPE_PASSWORD

# publish snapshot (using version in gradle.properties)
if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
	echo "publishing snapshot to public repository"
	./gradlew publishAllPublicationsToSnapshotRepository
fi

# from tag publish release to maven central using tag name as version
if [ -n "$TRAVIS_TAG" ];
then 
	echo "publishing release $TRAVIS_TAG to public repository"
	# NOTE that version is overriden here by the tag name
	./gradlew publishAllPublicationsToReleaseRepository -Pversion=$TRAVIS_TAG
fi