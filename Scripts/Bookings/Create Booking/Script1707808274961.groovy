import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

def response = WS.sendRequest(findTestObject('Object Repository/auth/POST Authenticate'))
GlobalVariable.TOKEN = WS.getElementPropertyValue(response, 'token')

def createBooking = WS.sendRequest(findTestObject(
	'Object Repository/bookings/POST Create Booking',
	[
		"firstname" : "Jalu",
		"lastname" : "Potter",
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