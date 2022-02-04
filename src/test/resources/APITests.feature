Feature: Testing API restful-booker
    
    
    @test
   Scenario: Verify Create Booking
    #Given Create Auth Token
    Then Call POST CreateBooking
    
    @test
     Scenario: Verify Get Booking - Booking By ID
    #Given Create Auth Token
    Then Call Get GetBookingIds
    
    @test
    Scenario: Verify Get Booking - All Bookings
    #Given Create Auth Token
    Then Call Get All GetBookingIds
    
    @test
    Scenario: Verify Get Booking - Search By firstname
    #Given Create Auth Token
    Then Search Booking by firstname
    
    @test
    Scenario: Verify Get Booking - Search By lastname
    #Given Create Auth Token
    Then Search Booking by lastname
    
     @test
    Scenario: Verify Get Booking - Search By Name
    #Given Create Auth Token
    Then Search Booking by name
    
   # Scenario: Verify Get Booking - Search By CheckIn/Checkout Date
    #Given Create Auth Token
   # Then Call POST CreateBooking 
    
    
    #Scenario: Verify Get Booking - BookingID UnKnown
    #Given Create Auth Token
    #Then Call POST CreateBooking 
    
    #Scenario: Verify Update Booking 
    #Given Create Auth Token
    #Then Call POST CreateBooking 
    
   # Scenario: Verify Update Partial Booking 
    #Given Create Auth Token
   # Then Call POST CreateBooking 
    
   # Scenario: Verify Delete Booking 
    #Given Create Auth Token
   # Then Call POST CreateBooking 