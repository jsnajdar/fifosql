# FIFO and SQL database test

In this test I will illustrate usage of the FIFO queue to maintain User objects in an SQL database.

* The **User** contains user data with the id, guid and name attributes
* The **Command** represents a message with the method, payload and payload type
    * **USER payload type** should procces **User** objects
    * **ADD method** with a User object payload will persist User to the SUSERS table 
    * **PRINT_ALL method** without payload will select all users from SUSERS table 
    * **DELETE_ALL method** without payload will delete all users from SUSERS table 
* Commands are sent into a **CommandQueue** using the **CommandProducer** and received by a **CommandConsumer**
    * **CommandQueueBlocking** implementation uses JDK **LinkedBlockingDeque**
    * **CommandQueueNonBlocking** implementation uses JDK **ConcurrentLinkedQueue**
* CommandConsumer postpone received object to the **CommandService**
    * It uses **CommandServiceFactory** implementation to determine CommandService and **Repository** implementations by the **payload type**
    * CommandService implementation dispatches command according to the **method** and calls appropriate Repository method
* **Repository** is responsible for insertion, selection or deletion in a database
    * **UserJdbcRepository** encapsulates User persistence using the JDBC connection
    * JDBC connection pool is maintained by the **EmbeddedH2Database** singleton which use embedded **H2 SQL database**


###Requirements

* JDK 11
* Maven


###Run

build and tests

	mvn clean install

tests

	mvn test

