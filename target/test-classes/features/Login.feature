@regression @dataFile:env/dev/data/userData.json
Scenario: Login with Json data
  Given I am on the login page
  When the user logs in with username "" and password ""
  Then user should see the dashboard header text "Products"
  Then user print address information from "" section of the data file
