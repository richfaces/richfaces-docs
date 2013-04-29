
#!/bin/bash

SCRIPT_DIR=`dirname $(readlink -f $0)`
ASCIIDOC_IMPL=$SCRIPT_DIR/../../../asciidoctor/bin/asciidoctor
TEMPLATE_DIR=$SCRIPT_DIR/../backend/slim/docbook45

convert_asciidoc() {
    BASENAME=$1
    BASE_DIR=$SCRIPT_DIR/../../../../$BASENAME/src/main/docbook/en-US/
    BASE_XML=$BASE_DIR/$BASENAME.asciidoc.xml
    BASE_AD=$BASE_DIR/$BASENAME.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -T$TEMPLATE_DIR \
                   -o$BASE_XML $BASE_AD \
                && xmllint --format $BASE_XML -o $BASE_XML
}

convert_asciidoc 'Developer_Guide'
convert_asciidoc 'Component_Reference'