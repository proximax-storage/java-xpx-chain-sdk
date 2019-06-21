#!/usr/bin/env sh

# signing key needs to be decoded from base64 to PGP armored text format
export ORG_GRADLE_PROJECT_signingKey="`echo $GPG_SECRET_KEYS | base64 -d`"
# password is used as is
export ORG_GRADLE_PROJECT_signingPassword=$GPG_PASSPHRASE

# sonatype publishing credentials
export ORG_GRADLE_PROJECT_sonatypeUsername=$SONATYPE_USERNAME
export ORG_GRADLE_PROJECT_sonatypePassword=$SONATYPE_PASSWORD

# download dependencies (at least most of them)
./gradlew downloadDependencies