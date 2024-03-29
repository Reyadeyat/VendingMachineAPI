```
#Changes on version 0.0.1
- define user roles
- add user role 'sales'
- add user role 'buyer'
- REST API:
	0- Content-Type: application/json
	1- 'buyer' deposit coins
	2- 'buyer' make purchases with set of cent coins {5, 10, 20, 50, 100]
	3- Data Structures:
		I  - ProductModel:
			0- productid: number //auto increment
			a- productmodel: string
			b- amountAvailable: number
			c- cost: DECIMAL(12,3)
			d- productName: unique string
			e- sellerId: number
		II - UserModel:
			0- userid: number //auto increment
			a- username: unique string
			b- password: string
			c- deposit: number //set of multiples of 5 cents
			e- role: string //'buyer' or 'seller'
	4- CRUD 'UserModel': (POST shouldn't require authentication)
		a- /crud/user_model: will response with user access token
			1- Request:
				a- POST 'seller': REQUEST: {"username": "seller_1", "password": "123", "deposit": null, "role": "seller"},
								 RESPONSE: {"username": "seller_1", "role": "seller", "balance": BALANCE, "access_token": "ACCESS_TOKEN"}
				b- POST 'buyer': {"username": "buyer_1", "password": "123", "deposit": 1000, "role": "buyer"}
								RESPONSE: {"username": "buyer_1", "role": "buyer", "balance": BALANCE, "access_token": "ACCESS_TOKEN"}
				c- Test GET 'USER': {"access_token": "ACCESS_TOKEN"}
								RESPONSE: {"username": "buyer_1", "role": "buyer", "balance": BALANCE, "access_token": "ACCESS_TOKEN"}
	5- CRUD 'ProductModel':
		a- GET: called by either 'seller' or 'buyer'.
		b- {POST, PUT, DELETE} called by the 'seller' supervises the ProductModel (UserModel.role='sellect').
	6- Endpoints:
		a- /deposit: 'buyer' can deposit {5, 10, 20, 50, 100} cent coins into their vending machine account (UserModel.deposit)
			1- Request: {"deposit": 100, "access_token": "ACCESS_TOKEN"}
			2- Response: {"username": "USER_NAME", role: "ROLE", balance: BALANCE}
		b- /buy: 'buyer' can buy products with deposited money into their vending machine account (UserModel.deposit):
			1- Request: {"productId": 3, "amount_of_products": 10, "access_token": "ACCESS_TOKEN"}
			2- Response: {"total_spent": "PURCHASED_PRODUCT_NAME", "change": of set {5, 10, 20, 50 and 100} cent coins}
		c- /reset 'buyer' can reset deposit:
			1- Request: {"deposit": 1000, "access_token": "ACCESS_TOKEN"}
- Note/Test/Run:
	0- Time spent working on this task 6 hours:
		a- reading.
		b- creating this changle log
		c- coding
		d- testing
		e- GitHub
	1- IDE: Apache NetBeans IDE 20
	2- Java: 21.0.1; OpenJDK 64-Bit Server VM 21.0.1+12-29
	3- OS: Linux kernel version 6.7.1
	4- Models cached in memory for sake of task
	5- Model Data will be released from cache after 5 minutes.
	6- Logical Exceptions are reported on http response code 400
	7- Rutntime Exceptions are reported using Tomcat Container Logginig System
	8- Logging APIs used to log transactions to Tomcat Log files
	9- Save "access_token": "ACCESS_TOKEN" in response to /crud/user_model to use it in blow test commands
	10- URL to test endpoints:
		a- /crud/user_model:
			1- POST 'seller':
				curl -k -X POST -H "Content-Type: application/json" -d '{"username": "seller_1", "password": "123", "deposit": null, "role": "seller"}' https://reyadeyat.net/vending_machine/crud/user_model
			2- POST 'buyer':
				curl -k -X POST -H "Content-Type: application/json" -d '{"username": "buyer_1", "password": "123", "deposit": 1000, "role": "buyer"}' https://reyadeyat.net/vending_machine/crud/user_model
			3- GET 'USER'
				curl -k -X POST -H "Content-Type: application/json" -d '{"access_token": "UUID FROM 1 or 2"}' https://reyadeyat.net/vending_machine/crud/user_model
		b- /crud/product_model:
		NOTE: sellerId changed to access_token for challenge authentication bonus
			1- POST 'product_a':
				curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 20, "productName": "product_name_1_1", "access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			2- POST 'product_b':
				curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 50, "productName": "product_name_1_2", "access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			3- POST 'product_c':
				curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_2", "amountAvailable": 75, "cost": 10, "productName": "product_name_2_1", "access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			3- {{ TEST buyer id 2 try to insert product_Q }} POST 'product_Q':
				curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_Q", "amountAvailable": 75, "cost": 5, "productName": "product_name_Q", "access_token": "BUYER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			4- PUT 'product_d':
				curl -k -X PUT -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 5, "productName": "product_name_1", "access_token": ""SELLER_ACCESS_TOKEN""}' https://reyadeyat.net/vending_machine/crud/product_model
			5- DELETE 'product_d':
				curl -k -X DELETE -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 5, "productName": "product_name_1", "access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
				a- try delete using buyer access_token
				curl -k -X DELETE -H "Content-Type: application/json" -d '{"productName": "product_name_1", "access_token": "BUYER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
				b- delete using seller access_token
				curl -k -X DELETE -H "Content-Type: application/json" -d '{"productName": "product_name_1", "access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			6- GET products by seller:
				curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "SELLER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
			7- GET products by buyer:
				curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "BUYER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/crud/product_model
		c- /deposit:
			curl -k -X POST -H "Content-Type: application/json" -d '{"deposit": 50, "access_token": "BUYER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/deposit
		d- /buy:
			curl -k -X POST -H "Content-Type: application/json" -d '{"product_list": ["product_name_1_1", "product_name_1_2", "product_name_2_1", "product_name_1"], "access_token": "BUYER_ACCESS_TOKEN"}' https://reyadeyat.net/vending_machine/buy
		e- /reset:
			It is not clear what reset shall do exactly since resetting deposit required some financial transaction that is not well defined to be implemented.
	
	11- Working Sample:

a- /crud/user_model:
	
#Create Seller
$curl -k -X POST -H "Content-Type: application/json" -d '{"username": "seller_1", "password": "123", "deposit": null, "role": "seller"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "userid": 1,
  "username": "seller_1",
  "role": "seller",
  "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942" ===> FIND REPLACE THIS ACCESS TOKEN WITH THE ONE YOU GET ON YOUR TERMINAL
}
#Try to recreate same Seller
$curl -k -X POST -H "Content-Type: application/json" -d '{"username": "seller_1", "password": "123", "deposit": null, "role": "seller"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "ERROR": "USER 'seller_1' ALREADY EXISTS"
}
#Get Seller
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "userid": 1,
  "username": "seller_1",
  "role": "seller",
  "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"
}

#Create Buyer
$curl -k -X POST -H "Content-Type: application/json" -d '{"username": "buyer_1", "password": "123", "deposit": null, "role": "buyer"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "userid": 2,
  "username": "buyer_1",
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca" ===> FIND REPLACE THIS ACCESS TOKEN WITH THE ONE YOU GET ON YOUR TERMINAL
}

#Try to recreate same Buyer
$curl -k -X POST -H "Content-Type: application/json" -d '{"username": "buyer_1", "password": "123", "deposit": null, "role": "buyer"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "ERROR": "USER 'buyer_1' ALREADY EXISTS"
}
#Get Buyer
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "userid": 2,
  "username": "buyer_1",
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"
}

#Get User that not exists
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca-XYZ"}' https://reyadeyat.net/vending_machine/crud/user_model
>>>>
{
  "ERROR": "USER NOT EXIST"
}


b- /crud/product_model:

#1- POST 'product_a':
$curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 20, "productName": "product_name_1_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "productid": 1,
  "productmodel": "product_model_1",
  "amountAvailable": 75,
  "cost": 20,
  "productName": "product_name_1_1",
  "sellerId": 1,
  "Action": "post"
}
#try to create product_a again
$curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 20, "productName": "product_name_1_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "ERROR": "PRODUCT [ product_name_1_1 ] ALREADY EXISTS"
}

#2- POST 'product_b':
$curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 50, "productName": "product_name_1_2", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "productid": 2,
  "productmodel": "product_model_1",
  "amountAvailable": 75,
  "cost": 50,
  "productName": "product_name_1_2",
  "sellerId": 1,
  "Action": "post"
}

#3- POST 'product_c':
$curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_2", "amountAvailable": 75, "cost": 10, "productName": "product_name_2_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "productid": 3,
  "productmodel": "product_model_2",
  "amountAvailable": 75,
  "cost": 10,
  "productName": "product_name_2_1",
  "sellerId": 1,
  "Action": "post"
}

#3- {{ TEST buyer access_token try to insert product_Q }} POST 'product_Q':
$curl -k -X POST -H "Content-Type: application/json" -d '{"productmodel": "product_model_Q", "amountAvailable": 75, "cost": 5, "productName": "product_name_Q", "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "userid": 2,
  "username": "buyer_1",
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "ERROR": "PRODUCT USER IS BUYER - NO PRODUCT CREATED" ========================> USER IS TYPE BUYER NOT SELLER
}


#4- PUT 'product_d':
$curl -k -X PUT -H "Content-Type: application/json" -d '{"productmodel": "product_model_1", "amountAvailable": 75, "cost": 5, "productName": "product_name_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "productid": 4,
  "productmodel": "product_model_1",
  "amountAvailable": 75,
  "cost": 5,
  "productName": "product_name_1",
  "sellerId": 1,
  "Action": "put" ============================> ACTION IS PUT
}


#5- DELETE 'product_d':
#try delete using buyer access_token
$curl -k -X DELETE -H "Content-Type: application/json" -d '{"productName": "product_name_1", "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "userid": 2,
  "username": "buyer_1",
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "ERROR": "PRODUCT USER IS BUYER - NO PRODUCT DELETED"
}


#delete using seller access_token
$curl -k -X DELETE -H "Content-Type: application/json" -d '{"productName": "product_name_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "productid": 5,
  "productName": "product_name_1_1",
  "sellerId": 1,
  "Action": "deleted"
}

#try delete the already deleted product using seller access_token
$curl -k -X DELETE -H "Content-Type: application/json" -d '{"productName": "product_name_1", "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "ERROR": "PRODUCT [ product_name_1_1 ] NOT EXISTS"
}


#6- GET products by seller user role:
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "Action": "get product list",
  "user": {
    "userid": 1,
    "username": "seller_1",
    "deposit": 0,
    "role": "seller",
    "access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"
  },
  "product_list": [
    {
      "productid": 2,
      "productmodel": "product_model_1",
      "amountAvailable": 75,
      "cost": 50,
      "productName": "product_name_1_2",
      "sellerId": 1
    },
    {
      "productid": 3,
      "productmodel": "product_model_2",
      "amountAvailable": 75,
      "cost": 10,
      "productName": "product_name_2_1",
      "sellerId": 1
    },
    {
      "productid": 1,
      "productmodel": "product_model_1",
      "amountAvailable": 75,
      "cost": 20,
      "productName": "product_name_1_1",
      "sellerId": 1
    }
  ]
}

#7- GET products by buyer user role:
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/crud/product_model
>>>>
{
  "Action": "get product list",
  "user": {
    "userid": 2,
    "username": "buyer_1",
    "deposit": 450,
    "role": "buyer",
    "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"
  },
  "product_list": [
    {
      "productid": 2,
      "productmodel": "product_model_1",
      "amountAvailable": 75,
      "cost": 50,
      "productName": "product_name_1_2",
      "sellerId": 1
    },
    {
      "productid": 3,
      "productmodel": "product_model_2",
      "amountAvailable": 75,
      "cost": 10,
      "productName": "product_name_2_1",
      "sellerId": 1
    },
    {
      "productid": 1,
      "productmodel": "product_model_1",
      "amountAvailable": 75,
      "cost": 20,
      "productName": "product_name_1_1",
      "sellerId": 1
    }
  ]
}


c- /deposit: 

#RUN TWICE
$curl -k -X POST -H "Content-Type: application/json" -d '{"deposit": 100, "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/deposit
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 100,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 0
}
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 200,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 100
}

#RUN TWICE
$curl -k -X POST -H "Content-Type: application/json" -d '{"deposit": 75, "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/deposit
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 275,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 200
}
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 350,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 275
}

#RUN TWICE
$curl -k -X POST -H "Content-Type: application/json" -d '{"deposit": 50, "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/deposit
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 400,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 350
}
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 450,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "Action": "deposit",
  "Prvious_Balance": 400
}


d- /buy:
$curl -k -X POST -H "Content-Type: application/json" -d '{"product_list": ["product_name_1_1"], "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/buy
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 450,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "product_list_response": [
    {
      "product_name": "product_name_1_1",
      "product_cost": 20
    }
  ],
  "Action": "deposit",
  "Prviouse_Balance": 450,
  "Current_Balance": 430,
  "Total Cost": 20
}

$curl -k -X POST -H "Content-Type: application/json" -d '{"product_list": ["product_name_1_1", "product_name_1_2"], "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/buy
{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 430,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "product_list_response": [
    {
      "product_name": "product_name_1_1",
      "product_cost": 20
    },
    {
      "product_name": "product_name_1_2",
      "product_cost": 50
    }
  ],
  "Action": "deposit",
  "Prviouse_Balance": 430,
  "Current_Balance": 360,
  "Total Cost": 70
}

$curl -k -X POST -H "Content-Type: application/json" -d '{"product_list": ["product_name_1_1", "product_name_1_2", "product_name_2_1"], "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/buy

#REPEAT UNTIL INSUFFICIENT FUND REPONSE

{
  "userid": 2,
  "username": "buyer_1",
  "deposit": 40,
  "role": "buyer",
  "access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca",
  "ERROR": "BuyerBuy INSUFFICIENT BALANCE - TRANSACTION CANCELED"
}

e- /reset: is not well defined



#WAIT 5 minutes and system will remove cached users and products

#GET products by seller user role:
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "c223e0ce-49a4-442d-885b-a689ba18a942"}' https://reyadeyat.net/vending_machine/crud/product_model
{
  "ERROR": "NO PRODUCTS TO LIST"
}

#GET products by buyer user role:
$curl -k -X GET -H "Content-Type: application/json" -d '{"access_token": "df7199c0-388b-4719-ad78-6cfb3d6734ca"}' https://reyadeyat.net/vending_machine/crud/product_model
{
  "ERROR": "NO PRODUCTS TO LIST"
}

```
