#!/bin/bash
#
# Script to bundle all docs
# Entire RichFaces source must be checked out to build the CDK Guide

DOCSDIR=$(cd $(dirname "$0"); pwd)

echo "[BUNDLE_DOCS] Enter version number: "
read VERSION
echo "[BUNDLE_DOCS] Creating bundle directory..."
mkdir $DOCSDIR/$VERSION

echo "[BUNDLE_DOCS] Building docbook docs..."
mvn clean install

echo "[BUNDLE_DOCS] Copying docs to bundle:"
echo "[BUNDLE_DOCS] -> Copying Component Reference..."
mkdir $DOCSDIR/$VERSION/Component_Reference
cp -r $DOCSDIR/Component_Reference/target/docbook/publish/* $DOCSDIR/$VERSION/Component_Reference/
echo "[BUNDLE_DOCS] -> Copying Developer Guide..."
mkdir $DOCSDIR/$VERSION/Developer_Guide
cp -r $DOCSDIR/Developer_Guide/target/docbook/publish/* $DOCSDIR/$VERSION/Developer_Guide/

# echo "[BUNDLE_DOCS] Zipping bundle..."
# cd $DOCSDIR/$VERSION/
# zip -r $VERSION.zip *

echo "[BUNDLE_DOCS] Bundle complete."
