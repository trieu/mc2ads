#!/bin/sh
java -jar -Xms256m -Xmx1024m -XX:-UseParallelGC -XX:+DisableExplicitGC -XX:MaxPermSize=512m -verbose:gc rest-api-server.jar