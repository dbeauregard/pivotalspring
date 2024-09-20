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
        - ...
        - Deploy to production!
- Clean up and add mocks to testing

## Testing (Postman/http)
- [Get All] http localhost:8080/houses
- [Get One, valid] http localhost:8080/houses/2
- [Get One, 404] http localhost:8080/houses/99 [gives 404]
- [Add One] http post localhost:8080/houses address='1234 street' price=123456
- [Update One] http put localhost:8080/houses/6 address="4321" price=4321111 id=6
- [Delete One] http delete localhost:8080/houses/5

## Running in Docker
- gradle clean build
- docker build --build-arg JAR_FILE=build/libs/\*.jar -t dbeauregard/pivotalspring .
- docker run -p 8080:8080 dbeauregard/pivotalspring