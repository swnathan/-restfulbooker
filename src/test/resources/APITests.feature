Feature: Testing API restful-booker
    
    
   Scenario: Verify Create Booking
    Then Call POST CreateBooking
    
    Scenario: Verify Get Booking - Booking By ID
    Then Call Get GetBookingIds
 
    Scenario: Verify Get Booking - All Bookings
    Then Call Get All GetBookingIds
    
    Scenario: Verify Get Booking - Search By firstname
    Then Search Booking by firstname
     
    Scenario: Verify Get Booking - Search By lastname
    Then Search Booking by lastname
      
    Scenario: Verify Get Booking - Search By Name
    Then Search Booking by name
    
    Scenario: Verify Get Booking - Search By Dates (CheckIn,CheckOut)
    Then Search Booking by Dates         
 
    Scenario: Verify Update Booking - Valid Token
    Given Create Auth Token
    Then Call UpdateBookings for valid
    
    Scenario: Verify Update Booking - Invalid Token
    Given Create Auth Token
    Then Call UpdateBookings for invalid
    
    Scenario: Verify Partial Update Booking  - Valid Token
    Given Create Auth Token
    Then Call PartialUpdateBookings for valid
    
    Scenario: Verify Partial Update Booking  - Invalid Token
    Given Create Auth Token
    Then Call PartialUpdateBookings for invalid         
         
   Scenario: Verify Delete Booking  - Valid Token
   Given Create Auth Token 
   Then Call DeleteBooking for valid
   
   Scenario: Verify Deleted Booking  - Invalid Token
   Given Create Auth Token
   Then Call DeleteBooking for invalid