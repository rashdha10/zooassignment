Feature: Zoo plus assignment

Scenario Outline: Add an item to the cart and change the shipping country thereby validate the total with subtotal post the change of shipping country.
Given that user is on cartpage
When user add the products from the recommended section
Then user should be on overview page
And user adds few more products from the recommended section '<count>'
Then display the prices of the products added in an order 'Descending'
When user increments the products from lowest price
And delete the product with highest price
Then the total and subtotal should be correct with shipping charge
When user changes the shipping country '<country>'
And add the postal code '<postalcode>'
Then the total and subtotal should be correct with shipping charge
Examples:
|count|country|postalcode|
|1|Poland|5000|
