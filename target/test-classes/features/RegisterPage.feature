@registerPage
Feature: User registration
  In order to create a new account
  As a visitor of the automation practice site
  I want to complete the registration form and submit it successfully

  Background:
    Given I am on the registration page

  Scenario: Register successfully with valid user details
    When I enter valid registration details
    And I submit the registration form
    Then I should see a successful registration confirmation

  Scenario Outline: Fail registration with invalid email address
    When I enter registration details with email "<email>"
    And I submit the registration form
    Then I should see an email validation error

    Examples:
      | email                 |
      | plainaddress          |
      | user@invalid-domain   |
      | user.name@.com        |

  Scenario: Fail registration when required field is missing
    When I enter registration details without the first name
    And I submit the registration form
    Then I should see a required field error for first name

  Scenario: Register with multiple hobbies and country selection
    When I enter valid registration details
    And I select the gender as "Male"
    And I choose the hobbies "Cricket, Movies"
    And I select "India" as the country
    And I submit the registration form
    Then the registration should be accepted

  Scenario: Fail registration when passwords do not match
    When I enter valid registration details with password "Password123" and confirm password "Password321"
    And I submit the registration form
    Then I should see a password mismatch error
