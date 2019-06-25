#!/usr/bin/env sh

# abort on error immediately
set -e

# directory where static docs are located
DOC_DIR=docs
# generated javadoc location
JAVADOC_DIR=build/docs/javadoc
# directory where the pages content is created
PAGES_DIR=build/gh-pages

echo "copying static files"
cp -r $DOC_DIR $PAGES_DIR
# move new javadoc to the destination
echo "copying new javadoc"
mv $JAVADOC_DIR $PAGES_DIR