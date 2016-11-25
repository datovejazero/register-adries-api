## Requirements 
* java-8
* scala 2.11
* sbt
* SQLServer

## Build 
* ``` sbt assembly ```

## Run Tests
* Integration 
    * ``` sbt integration:test ```
* Unit tests
    * ``` sbt test ```
    
## How to run
* sbt run --config configfile
* example config file can be found at ``` test/resources/myConfig.conf ```

