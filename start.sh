mvn -f ./AsyncStreamPipeline/pom.xml clean compile package  
mvn -f ./LocalAsyncStreamPipelineServer/pom.xml clean compile package
docker-compose build
docker-compose up

