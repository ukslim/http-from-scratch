#!/bin/sh

cd target/classes
while true; do 
   nc.traditional -l \
       -p 8081 \
       -c "$JAVA_HOME/bin/java org.ukslim.webfromscratch.protocol.Http"
done

