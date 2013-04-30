
#!/bin/bash

SCRIPT_DIR=`dirname $(readlink -f $0)`
PROJECT_DIR=$SCRIPT_DIR/../../../..
PROJECT_DIR=$(readlink -f $PROJECT_DIR)
ASCIIDOC_IMPL=$PROJECT_DIR/asciidoc/asciidoctor/bin/asciidoctor
TEMPLATE_DIR=$PROJECT_DIR/asciidoc/src/main/backend/slim/docbook45/

convert_asciidoc() {
    BASENAME=$1
    echo "Generating docbook xml for $BASENAME"
    SOURCE_DIR=$PROJECT_DIR/$BASENAME/src/main/docbook/en-US
    BASE_XML=$SOURCE_DIR/$BASENAME.asciidoc.xml
    BASE_AD=$SOURCE_DIR/$BASENAME.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -T $TEMPLATE_DIR \
                   -o $BASE_XML $BASE_AD \
                && xmllint --format $BASE_XML -o $BASE_XML
}

convert_asciidoc 'Developer_Guide'
convert_asciidoc 'Component_Reference'