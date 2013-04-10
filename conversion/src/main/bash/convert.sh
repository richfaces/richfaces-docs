#!/bin/bash

CONVERT_DIR=`dirname $(readlink -f $0)`
SAXON_JAR=$CONVERT_DIR/../xslt/d2a/saxon9he.jar
D2A_XSLT=$CONVERT_DIR/../xslt/d2a/d2a.xsl

convert_dev_guide() {
    echo "Conversion started: Developer Guide"
    date

    DEV_GUIDE_DIR=$CONVERT_DIR/../../../../Developer_Guide/src/main/docbook/en-US/
    DEV_GUIDE_XML=$DEV_GUIDE_DIR/Developer_Guide.xml
    DEV_GUIDE_AD=$DEV_GUIDE_DIR/Developer_Guide.asciidoc
    DEV_GUIDE_CONVERSION_LOG=$DEV_GUIDE_DIR/Developer_Guide.a2d.log

    java -jar $SAXON_JAR -xi -s:$DEV_GUIDE_XML -o:$DEV_GUIDE_AD $D2A_XSLT &> $DEV_GUIDE_CONVERSION_LOG
    cat $DEV_GUIDE_CONVERSION_LOG

    date
    echo "Conversion finished: Developer Guide"
}

convert_comp_ref() {
    echo "Conversion started: Component Reference"
    date

    COMP_REF_DIR=$CONVERT_DIR/../../../../Component_Reference/src/main/docbook/en-US/
    COMP_REF_XML=$COMP_REF_DIR/Component_Reference.xml
    COMP_REF_AD=$COMP_REF_DIR/Component_Reference.asciidoc
    COMP_REF_CONVERSION_LOG=$COMP_REF_DIR/Component_Reference.a2d.log

    java -jar $SAXON_JAR -xi -s:$COMP_REF_XML -o:$COMP_REF_AD $D2A_XSLT &> $COMP_REF_CONVERSION_LOG
    cat $COMP_REF_CONVERSION_LOG

    date
    echo "Conversion finished: Component Reference"
}

convert_dev_guide
convert_comp_ref
