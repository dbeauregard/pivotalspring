# Pivotal Spring Demo Applicaiton
This is a 'pivotal' (a.k.a., key) demo application for Spring.

## Todo:
- CI/CD
    - Clean
    - Any code generation for REST? (stubs/skeletons)?
    - Compile (javac)
    - Jar (aka Package, `jar`, fat-jar)
    - Run Tests (Junit) - post results
    - Javadoc/other docs (API? Swagger?)
    - Linting (code quality, find bugs, formatting, etc.)
    - Security Scanning (Static, dependancy, container)
    - Post Jar to artifactory
    - Build Container (CI or CD?)
    - Post OCI image to Repo
    - CD...
- Clean up and add mocks to testing

## Testing (Postman/curl/http)
- [Get All] curl -v localhost:8080/houses | jq
- [Get One, valid] curl -v localhost:8080/house/2 | jq
- [Get One, 404] curl -v localhost:8080/house/99 [gives 404]
- [Add One] curl -X POST localhost:8080/house -H 'Content-type:application/json' -d '{"address": "Samwise Gamgee", "price": "456123"}'
- [Update One ]curl -X PUT localhost:8080/house/5 -H 'Content-type:application/json' -d '{"id": "5","address": "Samwise", "price": "456123"}'
- [Delete One] curl -X DELETE localhost:8080/house/5

## Running in Docker
- gradle clean build
- docker build --build-arg JAR_FILE=build/libs/\*.jar -t dbeauregard/pivotalspring .
- docker run -p 8080:8080 dbeauregard/pivotalspring