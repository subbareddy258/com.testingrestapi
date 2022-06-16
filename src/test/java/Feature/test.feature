Feature: getUsers


  Scenario: Able to fetchs to get user details

    When client retrieve the get users with 'valid'
  Then the status code of 'getusers' Response is 200 and Response Body matches with "" swagger schema
    And The Error code and Error Response while user 'getusers' Displayed Correctly
      |ErrorCode |ErrorMessage|
      |1233      |The request not confiured properly|
