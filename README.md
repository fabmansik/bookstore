# BookStore API

BookStore uses REST and GraphQL APIs

## Databases



MySQL have 4 tables in "bookstore" database: book, order_book(relations table @ManyToMany), orders, user

application.yml file (MySQL):

```bash
  datasource:
    url: jdbc:mysql://localhost:3306/bookstore
    username: root
    password: root
```
MongoDB have 2 collections in "bookstore" database: activityLog, reviews_comments

application.yml file (MongoDB):

```bash
  data:
    mongodb:
      uri: mongodb+srv://root:root@cluster0.urvydqm.mongodb.net/?retryWrites=true&w=majority
      database: bookstore
```


## API Usage
/register @RequestBody - registration - UserController.java
```bash
*/example code:
URL: localhost:8080/register

{ 
    "username":"admin",
    "password":"admin123",
    "email":"admin@gmail.com",
    "phone": 730247423
} 
```

/login @RequestBody - logination (auth token) - AuthController.java
```bash
*/example code:
URL: localhost:8080/login

{
    "username": "admin", 
    "password":"admin123"
}
```
/refresh @RequestBody - (refresh token to get new auth) - AuthController.java
```bash
*/example code:
URL:localhost:8080/refresh

{
    "refresh":"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEVfVVNFUiIsInN1YiI6ImtpayIsImlhdCI6MTcwNTIzNDI0MiwiZXhwIjoxNzA1MjQxNDQyfQ.D9tvTRoqLazjsvnydtpCjMxnP9960DJOV5MibXsZqCM"
}
```
/api/books - REST API CRUD with books with @PathVariable and @RequestBody (no security)
```bash
*/example code:
URL: localhost:8080/api/books/2

```

/graphql - GraphQL API CRUD with books (no security)
```bash
*/example code:
URL: localhost:8080/graphql

```

/order @RequestBody @RequestHeader (authenticated) - create order with ids of books
```bash
*/example code:
URL: localhost:8080/order

[1,2]
```
/comments @RequestHeader @RequestBody @RequestParam (bookId)
```bash
*/example code:
URL: localhost:8080/comments?bookId=7

"Book is good"
```
/reviews @RequestHeader @RequestBody @RequestParam (bookId) @RequestParam (rating)
```bash
*/example code:
URL: localhost:8080/reviews?bookId=8&rating=8

"Book is great"
```


## Possible Upgrades
Roles and Security (did on my Auto Ria 2.0 project on https://github.com/fabmansik/Autoria-clone-2.0)

More CRUD operations

More Logs

## Testing
I never worked with Jupiter.  Need a little bit more time to get used to it.

Project made by Milan Somyk