# Pivotal Spring Demo Applicaiton [![CI with Gradle](https://github.com/dbeauregard/pivotalspring/actions/workflows/CI-gradle.yml/badge.svg?branch=main)](https://github.com/dbeauregard/pivotalspring/actions/workflows/CI-gradle.yml) [![CD to CloudRun](https://github.com/dbeauregard/pivotalspring/actions/workflows/CD-CloudRun.yaml/badge.svg?branch=main)](https://github.com/dbeauregard/pivotalspring/actions/workflows/CD-CloudRun.yaml)
This is a 'pivotal' (a.k.a., key) demo application for Spring.
The intent of this is a reasonably best practice/architecture proof of concept that 
scafolds a Spring Application including REST, WEB, Data (JDBC), AI, and a CI process.
This includes two subprojects, one for the server side and one for the client.

## Projects/Apps
- [Server](server)
- [Client](client)

## Todo:
- AI
    - Cleanup and Prompt Tuning
    - Streaming Response to UI
- CD
    - Versioning
    - Deploy container from dockerhub to production (CodeRun)
- Clean up and add mocks (Mockito) to testing
- Future epics to explore: 
    - Thymeleaf and HTMX
    - AOT, CDS, Native Images
    - Zipkin

## Testing (Postman/http)
- [Get All] http localhost:8080/houses
- [Get One, valid] http localhost:8080/houses/2
- [Get One, 404] http localhost:8080/houses/99 [gives 404]
- [Add One] http post localhost:8080/houses address='1234 street' price=123456
- [Update One] http put localhost:8080/houses/6 address="4321" price=4321111 id=6
- [Delete One] http delete localhost:8080/houses/5
- [Structured Output] http get localhost:8080/ai/structured message=="show largest house"
- * may need to add parameters: 'bdrm=3 bath=2 sqft=2000'

## Building & Running Locally
(uses docker compose for some tests; docker must be running)
- ./gradlew clean build
- ./gradlew bootRun 
- [w/ local postgres] ./gradlew bootRun --args='--spring.profiles.active=psql'

## Running in Docker
- gradle clean build
- docker build --build-arg JAR_FILE=build/libs/\*.jar -t dbeauregard/pivotalspring-server-dockerfile .
- docker run -p 8080:8080 dbeauregard/pivotalspring-server-dockerfile:latest

## Running in Kubernetes
- docker image must be pushed to dockerhub if not already
- kubectl deploy -f kubernetes.yaml

## Spring AI with Ollama
- brew install ollama
- ollama serve [runs ollama 'server']
- (new terminal, will install model first time) ollama run llama3.1 (or llama3.2) [`ctrl + d` to exit]
- (Open WebUI) docker run -d -p 3000:8080 --add-host=host.docker.internal:host-gateway -v open-webui:/app/backend/data --name open-webui --restart always ghcr.io/open-webui/open-webui:main
- [local postgres/pgvector] ./gradlew bootRun --args='--spring.profiles.active=psql,ai'

## Reference CI/CD Process
- CI/CD Process
    - Clean
    - Any code generation for REST (stubs/skeletons)?
    - Compile (javac)
    - Jar (aka Package, `jar`, fat-jar)
    - Run Unit and Component Tests (Junit) - post results
    - Javadoc/other docs (API? Swagger?)
    - Linting (checkstyle, find/spot-bugs, code coverage [JaCoCo], etc.)
    - Security Scanning (Static, dependancy, container)
    - Post Jar to artifact repository
    - Build OCI Container Image (CI or CD?)
    - Post OCI container image to Repo
    - Security Scanning of container image 
    - CD...
        - ...
        - Deploy to production!
