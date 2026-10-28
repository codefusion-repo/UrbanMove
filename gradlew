#!/usr/bin/env sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
 echo 'Falta gradle/wrapper/gradle-wrapper.jar. Copia el JAR oficial desde InventoryApp.' >&2
 exit 1
fi
exec java -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
