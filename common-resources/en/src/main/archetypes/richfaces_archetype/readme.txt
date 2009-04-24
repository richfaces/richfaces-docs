How to Use the richfacesguide Archetype
1.	Build the archetype with "mvn clean install"
2.	Cd to RichFaces_trunk\docs\. Run
mvn archetype:generate -DarchetypeGroupId=org.richfaces.docs -DarchetypeArtifactId=richfacesguide-archetype -DarchetypeVersion=3.3.1-SNAPSHOT -DartifactId=RichFacesGuide -DgroupId=org.richfaces.docs
(artifactId - project folder name).
When the build is finished you will see the RichFacesGuide(your project name folder). 
3.	Cd to RichFacesGuide (your project name folder) and "mvn clean install" to build the guide.
