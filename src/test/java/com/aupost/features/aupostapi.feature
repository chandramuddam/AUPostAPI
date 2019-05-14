Feature: Australia post API scenarios

  Scenario: Validate the domestic parcel postage cost for the given post codes and service code
    Given I find the first from post code for the suburb "Pendle Hill" and state "NSW"
    And I find the first to post code for the suburb "Strathfield" and state "NSW"
    And I find the first service code for the item length "10" width "10" thickness "10" weight "1"
    When I build the API request to calculate the postage cost for service code and length "10" width "10" height "10" weight "1"
    Then I validate the postage cost as "6.70"

  Scenario: Validate the list of domestic letter thickness values
    Given I build the API request for a list of domestic letter thickness values
    Then I validate the letter thickness values "5" and "20"

  Scenario: Validate the international parcel services for the country New Zealand
    Given I have "NZ" as country code
    And I have "1" as weight
    When I build the API request for international parcel services for New Zealand
    Then I validate the international parcel services for New Zealand


