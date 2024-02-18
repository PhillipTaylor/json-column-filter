
# json-column-filter

High performance column filtering of line based JSON objects into line based JSON objects

## This application is best shown by example

Given a json file as the input like this:

```json
{ "name" : "phill", "age" : 23, "gender" : "M" }
{ "name" : "loretta", "age" : 34, "gender" : "F" }
{ "name" : "ben", "age" : 1, "gender" : "M" }
```

And a "column file" (the apps second argument) like this:

```
name
age
```

Produces text to screen like this:

```json
{ "name" : "phill", "age" : 23 }
{ "name" : "loretta", "age" : 34 }
{ "name" : "ben", "age" : 1 }
```

## Running the app

```bash

java -jar json-filter-columns.jar inputfile.json columnlist.txt > output.json

# argument 1: The (already flattened) json file to remove columns from
# argument 2: A text file with a list of columns to keep on each line

# notice: the app doesn't take an output file so you need to redirect stdout to your usual bash skills
```

## Design

This application is written in Scala and uses the fs2 streaming library. It's streaming architecture is designed to work with large files.

## Options

If you have a slow machine you can try and tune the JVM `export JAVA_OPTS="-XX:+UseG1GC -Xmx4096m"`.

Java's slogan is "write-once, run anywhere". You can download the jar from the github releases and run it on any JVM > JDK 17.
