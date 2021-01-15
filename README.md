# track-shipment

Add **aftership-api-key** to Headers

## 1. Shipment Details POST API  
URL: http://localhost:8080/tracking

###### Request body: 
 {
   "origin": "990 S Hwy 395 S, Hermiston, OR, USA 97838",
   "destination" : "3505 Factoria Blvd SE, Bellevue, WA, USA 98006",
   "trackingNumber": "9374889676091297266845",
   "courierCode" : "USPS"
 } 
 
 API takes shipmentDetails as input and validates it. All fields are mandatory. After validating the request payload, checks whether the data exists in h2database with the combination of the tracking number and courier code. If the data present in h2database, "Tracking number already exists" exception will be thrown. 
 
 Otherwise, aftership post API will be called to store shipment details. If the response is a success from API, then the request will be store in h2database also for future checks else an exception will be thrown.
 
 ## 2. Shipment Details GET API  
URL: http://localhost:8080/tracking/{trackingNumber}/{courierCode}

###### Response body: 
 {
   "origin": "990 S Hwy 395 S, Hermiston, OR, USA 97838",
   "destination" : "3505 Factoria Blvd SE, Bellevue, WA, USA 98006",
   "trackingNumber": "9374889676091297266845",
   "courierCode" : "USPS"
 } 
 
 API takes trackingNumber and courierCode as inputs and validates them. Both are mandatory. After validating them, checks whether the record exists in h2database with the given combination. If the data doesn't exist in h2database, "Tracking not found" exception will thrown.
 
 Otherwise, aftership GET API will be called to fetch tracking details. If the response is a success from API then the details will return else an exception will be thrown.
