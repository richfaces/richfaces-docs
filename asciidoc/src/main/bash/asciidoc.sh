
#!/bin/bash

SCRIPT_DIR=`dirname $(readlink -f $0)`
ASCIIDOC_IMPL=$SCRIPT_DIR/../../../asciidoctor/bin/asciidoctor
TEMPLATE_DIR=$SCRIPT_DIR/../backend/slim/docbook45

convert_dev_guide() {
    DEV_GUIDE_DIR=$SCRIPT_DIR/../../../../Developer_Guide/src/main/docbook/en-US/
    DEV_GUIDE_XML=$DEV_GUIDE_DIR/Developer_Guide.asciidoc.xml
    DEV_GUIDE_AD=$DEV_GUIDE_DIR/Developer_Guide.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -T$TEMPLATE_DIR \
                   -o$DEV_GUIDE_XML $DEV_GUIDE_AD \
                && xmllint --format $DEV_GUIDE_XML -o $DEV_GUIDE_XML
}

convert_comp_ref() {
    COMP_REF_DIR=$SCRIPT_DIR/../../../../Component_Reference/src/main/docbook/en-US/
    COMP_REF_XML=$COMP_REF_DIR/Component_Reference.asciidoc.xml
    COMP_REF_AD=$COMP_REF_DIR/Component_Reference.asciidoc

    $ASCIIDOC_IMPL -a toc -a numbered -a docinfo -bdocbook -d book \
                   -T$TEMPLATE_DIR \
                   -o$COMP_REF_XML $COMP_REF_AD \
                && xmllint --format $COMP_REF_XML -o $COMP_REF_XML
}

convert_dev_guide
convert_comp_ref
