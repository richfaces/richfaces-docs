#!/bin/bash
# This script is used for the documentation release build
# Author: Gleb Galkin <ggalkin@exadel.com>

#REQUIREMENTS:
# If you use cygwin, please, check whether 'cpio' package is installed

#NOTE:
# If some docs were added you should add the pathes to the DOCS array:
# 1. proceed to the $TRUNK/ui/assembly directory 
# 2. open pom.xml file in your favorite text editor
# 3. find release profile with maven-dependency-plugin inside
# 4. add necessary artifactItem, e.g.
#	<artifactItem>
#		<groupId>org.richfaces.docs.userguide</groupId>
#		<artifactId>en</artifactId>
#		<version>${project.version}</version>
#		<type>jar</type>
#		<outputDirectory>${project.build.directory}/docs/userguide/en</outputDirectory>
#	</artifactItem>
# 5. finally you should add necessary docs formats to the DOCS array as follows:
# [n]="SRC=$TRUNK/ui/assembly/target/docs/userguide/en/html TARGET=$FREEZONE/devguide/en/html"
# where'n' is the next array index


# If you run this script for the first time or some docs were already added you could check all the pathes in the DOCS array. In order to check them take the following steps:
# 1. run the following command from the trunk root:  mvn clean install -P release,docs,release_docs -Dmaven.test.skip=true 
# 2. proceed to the $TRUNK/ui/assembly/target directory and check all pathes that have been defined in the DOCS array, SRC field. If some of them have been changed rewrite them manually 
# 3. finnally proceed to the $FREEZONE directory and check all pathes that have been defined in the DOCS array, TARGET field. If some of them have been changed rewrite them manually

##    Array of DOCs
##############################################################################################################
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
	[9]="SRC=$TRUNK/ui/assembly/target/docs/photo_album_app_guide/en/pdf TARGET=$FREEZONE/photo_album_app_guide/en/pdf"
	[10]="SRC=$TRUNK/ui/assembly/target/docs/photo_album_app_guide/en/html TARGET=$FREEZONE/photo_album_app_guide/en/html"
	[11]="SRC=$TRUNK/ui/assembly/target/docs/photo_album_app_guide/en/html_single TARGET=$FREEZONE/photo_album_app_guide/en/html_single"
)

##############################################################################################################


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
		
		#Try to commit files
		#printLog "Try to commit files..."
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

function validateInput () {
	if [ -d $@ ]
	then
	echo ""
	else
	die $@ ": No such directory"
	fi
}

echo -e "\n\r Attention! 
\r If you use cygwin, please, check whether 'cpio' package is installed!
\r If you run this script for the first time or some new docs were added to the build \r 
\r you should check all the pathes in the DOCS array! 
\r In order to check them, please, open this shell script in your favorite text editor and read NOTE section at the beginnig of the file."
while true
do
  echo -n "Are you sure to begin build process? [Y or N] :"
  read CONFIRM
  case $CONFIRM in
    y|Y|YES|yes|Yes) break ;;
    n|N|no|NO|No)
      echo "Thank you for you patience"
      exit
      ;;
    *) echo Please enter only y or n
  esac
done


echo -n "Define the ABSOLUTE path to the 'trunk' directory:"
read -e TRUNK 
validateInput $TRUNK
echo -n "Define the ABSOLUTE path to the 'freezone/doc':"
read -e FREEZONE 
validateInput $FREEZONE
echo -n "Define the ABSOLUTE path to the log file (e.g. /home/user/RFDocRelease.txt:"
read -e LOG 
echo -n "Specify your user name for commit:"
read -e USER 
read -s -p "Specify your password for commit: " PASS
echo ""
echo -n "Enter a message for commit:"
read -e MESSAGE

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
