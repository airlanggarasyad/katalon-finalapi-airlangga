# Restful-booker API Testing using Katalon Studio

This document outlines the API documentation for Restful-booker along with two test suites created using Katalon Studio. Created for hacktiv8's Quality Assurance learning path.

## Authentication
This section covers the endpoint for user authentication. The request includes sending credentials, and based on whether the credentials are correct or not, the response varies. If the credentials are correct, the server responds with an HTTP 200 OK status and includes a token in the response body. In case of incorrect credentials, the server still responds with an HTTP 200 OK status, but the response body contains a reason for bad authorization. These responses are crucial for verifying the authentication process in the Restful-booker API.

### Request
```groovy
GlobalVariable.USERNAME = "jalu"

def response = WS.sendRequest(findTestObject('Object Repository/auth/POST Authenticate'))

String responseBody = WS.getElementPropertyValue(response, 'token')

if(positiveCase){
  assert responseBody != null
} else {
  assert responseBody == null
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
  
## Booking
Within the booking section, there are cases for editing bookings and creating/deleting bookings.

### Edit Booking
For editing bookings, the request involves partially updating specific details of an existing booking. Upon sending the request, the server responds with details of the updated booking, including the changes made. This response includes various fields such as the new first name, last name, total price, deposit paid status, booking dates, and additional needs. Verifying these details ensures that the booking editing functionality is working as expected.
#### Request
```groovy
def response = WS.sendRequest(findTestObject('Object Repository/auth/POST Authenticate'))
GlobalVariable.TOKEN = WS.getElementPropertyValue(response, 'token')

def partialUpdate = WS.sendRequest(findTestObject(
	'Object Repository/bookings/PATCH Partial Update',
	[
		'id': 9,
		'firstname': 'Jalu',
		'lastname': 'Potter'
	]
))

if(WS.verifyResponseStatusCode(partialUpdate, 200)) {
	String newFirstname = WS.getElementPropertyValue(partialUpdate, 'firstname')
	String newLastname = WS.getElementPropertyValue(partialUpdate, 'lastname')
	
	assert newFirstname == 'Jalu'
	assert newLastname == 'Potter'
} else {
	assert false
}
```
#### Response
```json
HTTP/1.1 200 OK

{
    "firstname" : "Jalu",
    "lastname" : "Potter",
    "totalprice" : 111,
    "depositpaid" : true,
    "bookingdates" : {
        "checkin" : "2018-01-01",
        "checkout" : "2019-01-01"
    },
    "additionalneeds" : "Breakfast"
}
```

### Create and Delete Booking
In the case of creating and deleting bookings, the process starts with authenticating the user and obtaining a token. The request for creating a booking includes providing details such as first name, last name, price, check-in, check-out dates, and any additional needs. Upon successful creation, the server responds with an HTTP 200 OK status and includes the booking ID. Subsequently, the created booking is deleted using the obtained booking ID. The deletion request results in an HTTP 201 Created status if successful. These cases validate the functionality of creating and deleting bookings in the Restful-booker API.
#### Request
```groovy
def response = WS.sendRequest(findTestObject('Object Repository/auth/POST Authenticate'))
GlobalVariable.TOKEN = WS.getElementPropertyValue(response, 'token')

def createBooking = WS.sendRequest(findTestObject(
	'Object Repository/bookings/POST Create Booking',
	[
		"firstname" : "Jalu",
		"lastname" : "Goyang",
		"price" : 394,
		"isAlreadyPaid" : true,
		"checkin" : "2024-13-02",
		"checkout" : "2024-15-02",
		"additionals" : "Pake terminal"
	]
))

Integer bookingId = 0

if(WS.verifyResponseStatusCode(createBooking, 200)) {
	bookingId = WS.getElementPropertyValue(createBooking, 'bookingid')
} else {
	assert false
}

def deleteBooking = WS.sendRequest(findTestObject(
	'Object Repository/bookings/DELETE Delete Booking', 
	['id': bookingId]
))

WS.verifyResponseStatusCode(deleteBooking, 201)
```
#### Response
```json
HTTP/1.1 201 Created
```
