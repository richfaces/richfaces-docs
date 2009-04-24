How to use the richfacesguide archetype.
1.	Build the archetype with "mvn clean install"
2.	Cd to RichFaces_trunk\docs\. Run
mvn archetype:generate -DarchetypeGroupId=org.richfaces.docs -DarchetypeArtifactId=richfacesguide-archetype -DarchetypeVersion=3.3.1-SNAPSHOT -DartifactId=RichFacesGuide -DgroupId=org.richfaces.docs
(artifactId – project folder name)
When the build is finished you will see the RichFacesGuide. 
3.	Copy the RichFacesGuide to RichFaces_trunk\docs\
4.	Run "mvn clean install" to build the guide
