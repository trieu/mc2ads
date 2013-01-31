#!/bin/sh
java -jar -Xms256m -Xmx1g -XX:-UseParallelGC -XX:+DisableExplicitGC -verbose:gc rest-api-server.jar