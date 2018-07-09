
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Product Management
-------------------------------------------------------------------------------------------------------------------------------------------------------

Deployment Instructions

Prerequisite : 

	Run the project available at https://github.com/deepaksundar/my-retail-product-price-mgmt.git in the same machine where this project to be deployed or deploy it anywhere and configure the url host in appliction.proprties

	1) Replace the credentials of mongoDB, OAuth Server details, OAuth credentials in the application.properties file
	2) From command prompt navigate to the folder where the pom.xml file located
	3) Run the command mvn clean package
	4) Jar will be created in the folder ./target/**.jar
	5) Run the jar by the command java -jar target/**.jar
	6) In case of IDE, run the project as spring boot app
______________________________________________________________________________________________________________________________________

Configuration

port no : 8091 (configurable in application.properties)

MongoDB : This api connects to the mongoDB cloud (Atlas). The Connection string is configurable in application.properties (can be replaced with onprem DB as well)

OAuth : This Service uses OKTA as an Oauth provider to authenticate the users. Configurations can be found at application.properties 
________________________________________________________________________________________________________________________________________________________________________

Getting Access token from Okta

Run the following CURL command

curl -X POST \
  'https://dev-804238.oktapreview.com/oauth2/default/v1/token?grant_type=client_credentials&scope=myretail' \
  -H 'authorization: Basic MG9hZm9yNG9vbUZraDJPRVIwaDc6YUtPSUtmR1l0U1hpUDVsRWFGxxxxxxxBDV1lQNURwSGxPVXdKWg==' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'postman-token: 3016a9fb-70bc-6bf0-1060-ba88e19daf6e'
  
  authorization header is Basic (base64encode(clientId:clientSecret)
  
  the response will contain the access_token (JWT)
________________________________________________________________________________________________________________________________________________________________________

This API exposes the following endpoints

1) GET /products/
	
Description : This will respond with the complete set of all products available with its description and price

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
________________________________________________________________________________________________________________________________________________________________________

2) GET /products/"productId"
	
Description : This will respond with the product details(price, description) of the product with an id "productId"

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
________________________________________________________________________________________________________________________________________________________________________


3) GET /products/descriptions
	
Description : This will respond with the complete set of all products available with its description

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
________________________________________________________________________________________________________________________________________________________________________

4) GET /products/descriptions/"productId"
	
Description : This will respond with the description of the product with id "productId"

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
________________________________________________________________________________________________________________________________________________________________________

5) POST /products/descriptions
	
Description : This will create a new product with its description

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
	Content-Type = "application/json"

Request Body :
	{
        "id": 9099,
        "product_desc": "Shoes"
    }
_____________________________________________________________________________________________________________________________________________________________________
6) PUT /products/descriptions
	
Description : This will update a existing product's description

Required Headers :
	Accept = "application/json"
	Authorization = "Bearer -accessToken-"
	Content-Type = "application/json"

Request Body :
	{
        "id": 9099,
        "product_desc": "Shoes"
    }
_____________________________________________________________________________________________________________________________________________________________________
7) DELETE /products/descriptions/"productId"
	
Description : This will delete the product description of the product with an Id "productId"

Required Headers :
	Authorization = "Bearer -accessToken-"
________________________________________________________________________________________________________________________________________________________________________
