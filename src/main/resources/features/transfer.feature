Feature: Transfer mosaics and messages
  As Alice
  I want to make transfers to Bob
  So that Bob receives mosaics and messages

  Scenario: Send network currency
    Given "Alice" is granted 10.0 of "network currency"
     When "Alice" sends 8.5 of "network currency" to "Bob" with plaintext message "hi Bob" and fee 1.0
     Then "Alice" has 0.5 of "network currency"
      And "Bob" has 8.5 of "network currency"
