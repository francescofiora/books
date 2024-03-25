Feature: Rest Api

  Scenario: Verify admin user has access to Permissions and Roles
    Given the admin user
    When get the Permissions
    Then the GET status should be OK
    And the list of Role and Permission should be not empty
    When get the Roles
    Then the GET status should be OK
    And the list of Role and Permission should be not empty

  Scenario: Verify CRUD Roles operation 
    Given the admin user
    When get the Permissions
    And create a new Role
    Then should be able to get that Role
    And the GET status should be OK
    And the Role from POST should the same as from GET
    When update the Role
    Then the PUT status should be OK
    And should be able to get that Role
    And the Role from PUT should the same as from GET
    When delete the Role
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Role

  Scenario: Verify book admin user has not access to Permissions and Roles
    Given the book admin user
    Then should be not able to get the Permissions
    And should be not able to get the Roles

  Scenario: Verify CRUD Author operation
    Given the book admin user
    When create a new Author
    Then should be able to get that Author
    And the GET status should be OK
    And the Author from POST should the same as from GET
    When update the Author
    Then the PUT status should be OK
    And should be able to get that Author
    And the Author from PUT should the same as from GET
    When delete the Author
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Author

  Scenario: Verify CRUD Publisher operation
    Given the book admin user
    When create a new Publisher
    Then should be able to get that Publisher
    And the GET status should be OK
    And the Publisher from POST should the same as from GET
    When update the Publisher
    Then the PUT status should be OK
    And should be able to get that Publisher
    And the Publisher from PUT should the same as from GET
    When delete the Publisher
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Publisher

  Scenario: Verify CRUD Title operation
    Given the book admin user
    When create a new Author
    And create a new Publisher
    And create a new Title
    Then should be able to get that Title
    And the GET status should be OK
    And the Title from POST should the same as from GET
    When update the Title
    Then the PUT status should be OK
    And should be able to get that Title
    And the Title from PUT should the same as from GET
    When delete the Title
    Then the DELETE status should be NO_CONTENT
    And should be not able to get that Title
    When delete the Publisher
    Then the DELETE status should be NO_CONTENT
    When delete the Author
    Then the DELETE status should be NO_CONTENT

  Scenario: Verify reader user has not access to Permissions and Roles and cannot create Authors and Publishers
    Given the reader user
    Then should be not able to get the Permissions
    And should be not able to get the Roles
    And should be not able to create new Author
    And should be not able to create new Publisher

  Scenario: Verify reader user has access to Authors, Publishers and Titles
    Given the reader user
    When get the Authors
    Then the GET status should be OK
    When get the Publishers
    Then the GET status should be OK
    When get the Titles
    Then the GET status should be OK
    