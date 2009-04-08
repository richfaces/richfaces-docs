#!/bin/bash
# This script is used for the release build
# Author Gleb Galkin

#REQUIREMENTS:
# If you use cygwin, please, check whether 'cpio' package is installed

#Here you should define necessary variables:
# 1. define the absolute path to the 'trunk' directory in the TRUNK variable
# 2. define the absolute path to the 'freezone/doc' directory in the FREEZONE variable
#3. define  path and name of the log file in the LOG variable
#4. define user name for commit in the USER variable
#5. define password for commit in the PASS variable
#6. define message for commit in the MESSAGE variable

# If you run this script for the first time you could check all the pathes in the DOC array if it is really needed. In order to check them take the following steps:
# 1. run the following command from the trunk root:  mvn clean install -P release,docs,release_docs -Dmaven.test.skip=true 
# 2. proceed to the $TRUNK/ui/assembly/target directory and check all pathes that have been defined in the DOC array, SRC field. If some of them have been changed rewrite them manually 
# 3. finnally proceed to the $FREEZONE directory and and check all pathes that have been defined in the DOC array, TARGET field. If some of them have been changed rewrite them manually

TRUNK="D:/workspaceRF/trunk"
FREEZONE="D:/workspaceRF/online/freezone/docs"
LOG="log_release.txt"
USER="smukhina"
PASS=""
MESSAGE=""

DOCS=(
	[0]="SRC=$TRUNK/ui/assembly/target/apidocs TARGET=$FREEZONE/apidoc"
	[1]="SRC=$TRUNK/ui/assembly/target/tlddoc TARGET=$FREEZONE/tlddoc"
	[2]="SRC=$TRUNK/framework/api/target/site/apidocs TARGET=$FREEZONE/apidoc_framework"
	[3]="SRC=$TRUNK/ui/assembly/target/docs/migration/en TARGET=$FREEZONE/migrationguide/en"
	[4]="SRC=$TRUNK/ui/assembly/target/docs/cdkguide/en/ TARGET=$FREEZONE/cdkguide/en"
	[5]="SRC=$TRUNK/ui/assembly/target/docs/faq/en/html_single TARGET=$FREEZONE/devguide/en/faq"
	[6]="SRC=$TRUNK/ui/assembly/target/docs/userguide/en/html TARGET=$FREEZONE/devguide/en/html"
	[7]="SRC=$TRUNK/ui/assembly/target/docs/userguide/en/html_single TARGET=$FREEZONE/devguide/en/html_single"
	[8]="SRC=$TRUNK/ui/assembly/target/docs/userguide/en/pdf TARGET=$FREEZONE/devguide/en/pdf"
)

function die(){
	printLog "$@"
	exit 1
}

function cleanAndCopy () {
	for (( i=0;i<${#DOCS[@]};i++)); do
		doc="DOCS[i]"
		local ${!doc}
		cd $TARGET
		printLog "Deleting old files"
		find  -name .svn -prune -o \( -name \* -type f -print0 \)|  xargs -0 /bin/rm -f || die  "Something wrong with deleting old files. Please, see the log";
		cd $SRC
		printLog "Copying new files"
		find  -name .svn -prune -o \( -name \* -print0 \) | cpio -pmd0 $TARGET >> $LOG 2>&1 || die  "Something wrong with copying. Please, see the log"; 
	done
}

function delAddCi(){
	for (( i=0;i<${#DOCS[@]};i++)); do
		doc="DOCS[i]"
		local ${!doc}
		cd $TARGET
		#Try to add new files
		printLog "Try to add new files..."
		svn status | grep '^\?' | grep -o -P [^\?^" ""\n\r?"]+ | while read -r;do svn add $REPLY >> $LOG 2>&1; done || die "Something wrong with svn add. See the log file"

		#Try to delete missing files
		printLog "Try to delete unnecessary files..."
		svn status | grep '^\!' | grep -o -P [^\!^" ""\n\r?"]+ | while read -r;do svn rm $REPLY >> $LOG 2>&1; done || die "Something wrong with svn remove. See the log file"
		
		printLog "Try to commit files..."
		#svn commit --username $USER --password $PASS --message $MESSAGE >> $LOG 2>&1 || die "Something wrong with svn commit. See the log file"
	done
}

function printLog () {
	echo "$@"
	echo "/*-------------------------------------------------*/" >> $LOG
	echo >> $LOG
	echo "$@" >> $LOG
	echo >> $LOG
	echo "/*-------------------------------------------------*/" >> $LOG
	echo >> $LOG
}

if [ $TRUNK="" -o $FREEZONE="" -o $LOG="" -o $USER="" -o $PASS="" -o $MESSAGE="" ]
then
	echo "Please, specify necessary variables. See the header of this script"
	exit 1
fi

#Remove old log
if [ -e $LOG ]
then
	rm $LOG
fi

#Update the FREEZONE
cd $FREEZONE
printLog "Svn update $FREEZONE is in progress..."
svn up >> $LOG 2>&1 || printLog "Svn cleanup $FREEZONE will be performed and then I will try to update again."; svn cleanup; svn up >> $LOG 2>&1 || die "Something wrong with svn update in "$FREEZONE

#Update the trunk
cd $TRUNK
printLog "Svn update $TRUNK is in progress..."
svn up >> $LOG 2>&1 || printLog "Svn cleanup $TRUNK will be performed and then I will try to update again."; svn cleanup; svn up >> $LOG 2>&1 || die "Something wrong with svn update in "$TRUNK

#Docs, TLD and API docs
printLog "Docs, TLD and API building is in progress..."
mvn clean install -P release,docs,release_docs -Dmaven.test.skip=true >> $LOG 2>&1 || die "Something wrong with building trunk. Please, see the maven log"

# Framework API  building
cd $TRUNK/framework/api
printLog "Framework API building is in progress..."
mvn javadoc:javadoc >> $LOG 2>&1 || die "Something wrong with building API docs. Please, see the maven log"

#Copying all docs
printLog "Copying all docs..."
cleanAndCopy;

cd $FREEZONE/devguide/en/pdf
printLog "Renaming richfaces_reference.pdf to richfaces-usersguide.pdf"
mv richfaces_reference.pdf richfaces-usersguide.pdf

delAddCi;
