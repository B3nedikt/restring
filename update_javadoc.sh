#!/bin/bash

set -ex

# Lib constants
REPO="git@github.com:b3nedikt/restring.git"
GROUP_ID="com.b3nedikt.restring"
ARTIFACT_ID="restring"
VERSION="3.0.0"

# Javadoc constants
FILE_PATH="${GROUP_ID//./%2F}%2F${ARTIFACT_ID}%2F${VERSION}%2F${ARTIFACT_ID}-${VERSION}-javadoc.jar"
URL="https://bintray.com/b3nedikt/maven/download_file?file_path=$FILE_PATH"

# Script constants
TEMP_DIR=temp-clone
DOCS_DIR=docs
BRANCH_NAME=update_javadoc

# Delete temps
rm -rf $TEMP_DIR

# Clone the repo and create a new branch
git clone $REPO $TEMP_DIR
cd $TEMP_DIR
git checkout -b $BRANCH_NAME

# Delete docs
rm -rf $DOCS_DIR

# Download the latest javadoc
mkdir $DOCS_DIR
cd $DOCS_DIR
curl -L "$URL" > javadoc.zip
unzip javadoc.zip
rm javadoc.zip
cd ..

# Commit and push new javadoc
git add .
git add -u
git commit -m "Update javadoc at $(date)"
git push --set-upstream origin $BRANCH_NAME

# Cleanup
cd ..
rm -rf $TEMP_DIR