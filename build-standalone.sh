#!/bin/bash
if [ -z "$JAVA_HOME" ]; then
    echo "The variable JAVA_HOME must be set."
    exit 1
fi
rm -rf dist/
"$JAVA_HOME/bin/jlink" --module-path "target:$JAVA_HOME/jmods" \
    --add-modules com.lassilaiho.calculator \
    --launcher calculator=com.lassilaiho.calculator/com.lassilaiho.calculator.ui.App \
    --output dist \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages
