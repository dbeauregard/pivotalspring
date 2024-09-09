# Pivotal Spring Demo Applicaiton
This is a 'pivotal' (a.k.a., key) demo application for Spring.

## Todo:
- Setup Spring Profiles (different than data profiles?)
- Hook up to postgresql (need to drop and recreate)
- Enable Spring Security
- Web Page???
- CI/CD

## Testing (Postman/curl/http)
- [Get All] curl -v localhost:8080/houses | jq
- [Get One, valid] curl -v localhost:8080/house/2 | jq
- [Get One, 404] curl -v localhost:8080/house/99 [gives 404]
- [Add One] curl -X POST localhost:8080/house -H 'Content-type:application/json' -d '{"address": "Samwise Gamgee", "price": "456123"}'
- [Update One ]curl -X PUT localhost:8080/house/5 -H 'Content-type:application/json' -d '{"id": "5","address": "Samwise", "price": "456123"}'
- [Delete One] curl -X DELETE localhost:8080/house/5