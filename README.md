# Simple-JSON-Parser

>Implement a Simple JSON Parser that resolves JSON text completely and get specific information from it.


##How to use it?

1. Open this project in intellij
2. Click File->Project Sturcture->Artifact->Click "+"->JAR->From modules from dependencies...->OK
3. Build->Build Artifacts...->Build
4. Then you should read the Main.java code.
5. In the terminal

```
java -jar xxx.jar json_file_path

or

java -jar xxx.jar -pretty unordered_json_file_path

```

##Result

* If the json text is valid, the terminal will print "Valid".Else the terminal will print the error.

* If you enter the parameter "-pretty", then a new File will be created which named xxx.pretty.json

##Unfinished Module

* If you give the specific path such as "/RECODES[35]/countryname", if exist, return the value of that, otherwise, return null.
* ……


