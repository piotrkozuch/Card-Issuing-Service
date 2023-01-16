# Card Issuing Service

### Introduction
Spring Boot service that expose REST API for issuing cards. 

### How to build
To build this project run `docker-compose build`. It will build application jar along with docker image.

### How to run
To start this application run `docker-compose up`. Application will start and listen on `localhost:9001`


# REST API

Below you can find REST API description.

### 1. Create cardholder

### Request

`POST /api/v0.1/cardholders`

    curl -i -d '{"firstName": "Joe", "lastName": "Doe", "birthDate": "1967-11-27", "email": "joe.doe@test.pl", "phone": "+487904800700", "billingAddress": {"streetLine1": "ul. Jagiellońska", "streetLine2": "11a/1", "country": "PL", "postcode": "30-393", "city": "Kraków"}}' -H "Content-Type: application/json" -X POST http://localhost:9001/api/v0.1/cardholders

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 16 Jan 2023 23:32:42 GMT

    {"id":"02d6bc49-0572-44f3-8fe4-32eae23322df","firstName":"Joe","lastName":"Doe","birthDate":"1967-11-27","email":"joe.doe@test2.pl","phone":"+487904800700","state":"PENDING","billingAddress":{"streetLine1":"ul. Jagiellońska","streetLine2":"11a/1","city":"Kraków","country":"PL","postcode":"30-393"},"createdDate":"2023-01-16T23:41:43.816971879Z","updatedDate":"2023-01-16T23:41:43.816971879Z"}


### 2. Get all cardholders 

### Request

`GET /api/v0.1/cardholders`

    curl -i -H 'Accept: application/json' http://localhost:9001/api/v0.1/cardholders

### Response

    HTTP/1.1 200 OK
    Date: Thu, 24 Feb 2011 12:36:30 GMT
    Status: 200 OK
    Connection: close
    Content-Type: application/json
    Content-Length: 2

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 16 Jan 2023 23:29:41 GMT

    [{"id":"486627b8-d04a-4c92-9a11-b08f90f92e53","firstName":"Joe","lastName":"Doe","birthDate":"1967-11-27","email":"joe.doe@test.pl","phone":"+487904800700","state":"PENDING","billingAddress":{"streetLine1":"ul. Jagiellońska","streetLine2":"11a/1","city":"Kraków","country":"PL","postcode":"30-393"},"createdDate":"2023-01-16T23:28:46.023248Z","updatedDate":"2023-01-16T23:28:46.023248Z"}]

### 3. Get cardholder

### Request

`GET /api/v0.1/cardholders/:id`

    curl -i -H 'Accept: application/json' http://localhost:9001/api/v0.1/cardholders/486627b8-d04a-4c92-9a11-b08f90f92e53

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 16 Jan 2023 23:32:42 GMT

    {"id":"486627b8-d04a-4c92-9a11-b08f90f92e53","firstName":"Joe","lastName":"Doe","birthDate":"1967-11-27","email":"joe.doe@test.pl","phone":"+487904800700","state":"PENDING","billingAddress":{"streetLine1":"ul. Jagiellońska","streetLine2":"11a/1","city":"Kraków","country":"PL","postcode":"30-393"},"createdDate":"2023-01-16T23:28:46.023248Z","updatedDate":"2023-01-16T23:28:46.023248Z"}

### 4. Activate cardholder

### Request

`PATCH /api/v0.1/cardholders/:id/activate`

    curl -i -H 'Accept: application/json' http://localhost:9001/api/v0.1/cardholders/486627b8-d04a-4c92-9a11-b08f90f92e53/activate

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 16 Jan 2023 23:53:34 GMT

    {"id":"486627b8-d04a-4c92-9a11-b08f90f92e53","firstName":"Joe","lastName":"Doe","birthDate":"1967-11-27","email":"joe.doe@test.pl","phone":"+48700800900","state":"ACTIVE","billingAddress":{"streetLine1":"ul. Jagiellońska","streetLine2":"11a/1","city":"Kraków","country":"PL","postcode":"30-393"},"createdDate":"2023-01-16T23:28:46.023248Z","updatedDate":"2023-01-16T23:53:34.487946472Z"}

### 5. DELETE cardholder

### Request

`DELETE /api/v0.1/cardholders/:id`

    curl -i -X DELETE -H 'Accept: application/json' http://localhost:9001/api/v0.1/cardholders/486627b8-d04a-4c92-9a11-b08f90f92e53

### Response

    HTTP/1.1 200
    Content-Length: 0
    Date: Mon, 16 Jan 2023 23:56:08 GMT

