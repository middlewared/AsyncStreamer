mvn -f ./LocalAsyncStreamPipelineClient/pom.xml clean compile package
java -jar ./LocalAsyncStreamPipelineClient/target/LocalAsyncStreamPipelineClient-1.0-SNAPSHOT.jar 16000

