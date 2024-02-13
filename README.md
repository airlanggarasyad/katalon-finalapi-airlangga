# Restful-booker API Testing using Katalon Studio

This document outlines the API documentation for Restful-booker along with two test suites created using Katalon Studio.

## Authentication
Endpoint for user authentication.

### Request
```
POST ${GlobalVariable.URI}/auth
```

### HTTP Body
```json
{
  "username": "${GlobalVariable.USERNAME}",
  "password": "${GlobalVariable.PASSWORD}"
}
```

### Responses
- Correct credentials
  ```json
  HTTP/1.1 200 OK
  
  {
    "token": "${TOKEN}"
  }
  ```
- Incorrect credentials
  ```json
  HTTP/1.1 200 OK
  
  {
    "reason": "Bad authorization"
  }
  ```
