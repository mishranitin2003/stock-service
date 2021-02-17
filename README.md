# stock-service

A Stock Managing Microservice 
- To check the stock levels for the products sold by a retailer 
- Apply a number of ‘rules’ before returning advice for which products should be ordered
  - The input and output can be via any medium. Using RESTful request and JSON response for communication. Also, an intergration test in source code can be used.
  - Audit of update on the stock levels

Execution
- From Command line, navigate to the folder <Directory_On_Your_Local>/stock-service
  mvn spring-boot:run
- URI's 
  - Get Stock Recommendations: /recommendation/{productId}?retailer={retailer}
  - Update/PUT Minimum Stock level: /update/minStockLevel/{productId}/{minStockLevel}
  - Add/PUT to Current Stock level: /add/stockLevel/{productId}/{stockLevel}
  - Mark Product Blocked: /markBlocked/{productId}
