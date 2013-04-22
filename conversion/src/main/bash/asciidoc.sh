
#!/bin/bash

ASCIIDOC_IMPL="$(readlink -f $(which asciidoctor))"
CONVERT_DIR=`dirname $(readlink -f $0)`
SAXON_JAR=$CONVERT_DIR/../xslt/d2a/saxon9he.jar
D2A_XSLT=$CONVERT_DIR/../xslt/d2a/d2a.xsl
MY_JAVA_OPTS="-Xmx1024m -XX:MaxPermSize=512m"

convert_dev_guide() {
    DEV_GUIDE_DIR=$CONVERT_DIR/../../../../Developer_Guide/src/main/docbook/en-US/
    DEV_GUIDE_XML=$DEV_GUIDE_DIR/Developer_Guide.asciidoc.xml
    DEV_GUIDE_AD=$DEV_GUIDE_DIR/Developer_Guide.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -o$DEV_GUIDE_XML $DEV_GUIDE_AD \
                && xmllint --format $DEV_GUIDE_XML -o $DEV_GUIDE_XML
}

convert_comp_ref() {
    COMP_REF_DIR=$CONVERT_DIR/../../../../Component_Reference/src/main/docbook/en-US/
    COMP_REF_XML=$COMP_REF_DIR/Component_Reference.asciidoc.xml
    COMP_REF_AD=$COMP_REF_DIR/Component_Reference.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -o$COMP_REF_XML $COMP_REF_AD \
                && xmllint --format $COMP_REF_XML -o $COMP_REF_XML
}

convert_dev_guide
convert_comp_ref
