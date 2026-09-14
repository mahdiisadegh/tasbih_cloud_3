#!/usr/bin/env sh

#
# Copyright © 2015-2021 the original authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

if [ -n "$MSYSTEM" ]; then
    case "$MSYSTEM" in
        MINGW*|MSYS*) : ;;
        *) echo "Unsupported MSYSTEM: $MSYSTEM" >&2; exit 1 ;;
    esac
fi

APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set MAX_FD != -1 to use that value
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MINGW* | MSYS* ) msys=true ;;
esac

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    expr "$link" : '/.*' > /dev/null || link=`dirname "$PRG"`/"$link"
    PRG="$link"
done
SAVED="`dirname \"$PRG\"`"
APP_HOME=`cd "$SAVED" && pwd -P`

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/sh" ] ; then
        JAVA_HOME="$JAVA_HOME/bin/sh"
    else
        JAVA_HOME="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVA_HOME" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in the environment to match the
location of your Java installation."
    fi
else
    JAVA_HOME="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

exec "$JAVA_HOME" $args -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$ARGGLES"