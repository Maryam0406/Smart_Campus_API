Smart Campus API

Overview

The Smart Campus API is a strong, flexible backend web service that can handle university facilities and hardware.
This API is made entirely with Java and JAX-RS(Jakarta RESTful Web Services). 
It lets facilities managers and automated systems keep track of rooms and the many sensors (like CO2 monitors, occupancy trackers, and temperature sensors) that are spread out across the campus.
The system has strict business logic rules, can handle deep nesting with sub-resources, and has strong error handling to protect data integrity without using a regular database.

Setup & Build Instructions

This project uses Maven for dependency management and runs on an embedded Grizzly HTTP server.

Prerequisites:

    Java Development Kit (JDK) 11 or higher installed.

    Apache Maven installed and configured in your system PATH.

Setup & Build Instructions:

    Step 1: Start the Server

        Open the project folder in Apache NetBeans.

        Click the green "Run Project" button in the top toolbar.

        You will see Server started at http://localhost:8080/.

        Leave this terminal window open and running.
        Do not press Enter or close it or the server will shut down

    Step 2: Test the API

        Open a second new terminal window.

        Run the curl commands in this new window 

        After testing is completed, go back to the first window and press Enter to shut the server down.

API Usage Examples (cURL Commands)

Ensure the server is running before executing these commands in a new terminal window.

1. Discover API Capabilities

curl -X GET http://localhost:8080/api/v1

2. Register a New Room

curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d '{"id": "LIB-301", "name": "Library Quiet Study", "capacity": 50}'

3. Deploy a New Sensor to a Room

curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d '{"id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "currentValue": 21.5, "roomId": "LIB-301"}'

4. Filter Sensors by Type

curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"

5. Post a Historical Reading (Sub-Resource)

curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d '{"id": "read-001", "timestamp": 1713950000, "value": 23.4}'

6. Test 422 Unprocessable Entity (Missing Room Dependency)

curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d '{"id": "MOTION-001", "type": "Occupancy", "status": "ACTIVE", "currentValue": 0.0, "roomId": "GHOST-ROOM-999"}'

7. Test 403 Forbidden (State Constraint)

curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d '{"id": "CO2-001", "type": "CO2", "status": "MAINTENANCE", "currentValue": 400.0, "roomId": "LIB-301"}'

curl -X POST http://localhost:8080/api/v1/sensors/CO2-001/readings -H "Content-Type: application/json" -d '{"id": "read-002", "timestamp": 1713950100, "value": 415.0}'

8. Test 409 Conflict (Deletion Safety Logic)

curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301



Part 1: Service Architecture & Setup
1.	Project & Application Configuration
By default, JAX-RS resource classes are request-scoped, meaning the JAX-RS runtime creates a new instance of a resource class for every incoming HTTP request. After the response is sent, the instance is discarded. Because multiple requests can occur at the same time, data should not be stored in instance variables, as it will not be shared between requests. To maintain data across multiple requests, shared data structures such as HashMap or ArrayList are used, typically declared as static. However, since multiple threads can access these shared structures simultaneously, this can lead to issues such as race conditions or data inconsistency. To prevent these problems, thread-safe approaches must be used such as ConcurrentHashMap or synchronized collections. In this project, classes such as RoomResource, SensorResource, SensorReadingResource, and DiscoveryResource are JAX-RS resource classes, and a new instance of these classes is created for each incoming request.
2.	The Discovery Endpoint
HATEOS(Hypermedia As The Engine Of Application State) is considered advanced REST design because the server tells the client what actions can be done next by including links in the response. This implies that the client need not be fully informed in advance. This is helpful for client developers because it eliminates the need for them to review documentation and hardcode URLs constantly. Instead, they can use the links that the API provides. It also makes the system more flexible, because even if the server changes its URLs, the client can still work as long as the links in the response are correct.

Part 2: Room Management 
1.	Room Resource Implementation 
By returning only room IDs, less data is transmitted over the network, saving bandwidth and speeding up response times. However, this requires the client to make additional requests to retrieve full details for each room, which adds to the client's workload and may result in several additional requests. Returning entire objects, on the other hand, provides all the information required in a single response. This eliminates the need for extra requests, making it simpler for the client to process and show the data. It does, however, increase the size of the response, which may use more network bandwidth and slightly increase response time.

2.	Room Deletion & Safety Logic
Yes, the DELETE function is idempotent. Idempotency means that running the same request multiple times will leave the server in the same state as running it just once. If a client accidentally sends five DELETE requests for "LIB-301," the first one will work and remove the room from the HashMap. It will also return a success status code, like 204 No Content. The next four requests will look for "LIB-301" on the map, but they won't find it and will throw a RoomNotFoundException (mapped to a 404 Not Found). The HTTP status codes for the first and later requests are different, but the server's state stays the same: the room is still gone.

Part 3: Sensor Operations & Linking
1. Sensor Resource & Integrity 
When @Consumes is declared, it sets a strict contract for the MIME type of the HTTP request body that you expect. The JAX-RS runtime stops the request before it gets to the resource method's execution logic if a client sends a payload with a Content-Type header of text/plain or application/xml. The framework automatically stops the routing and sends an HTTP 415 Unsupported Media Type error when it sees that the types don't match. This built-in validation keeps backend logic from trying to parse data formats that don't work together, which stops runtime parsing exceptions .

2. Filtered Retrieval & Search 
Path parameters (@PathParam) are used to access specific, unique resources in a hierarchy, such as retrieving a sensor by its ID. In contrast, query parameters (@QueryParam) are used to filter, sort, or modify a collection of resources. The query approach (e.g., /sensors?type=CO2) is better because it keeps /sensors as a collection resource and allows flexible filtering. It also supports multiple optional filters, such as /sensors?type=CO2&status=ACTIVE. Using path-based filtering (e.g., /sensors/type/CO2) can lead to rigid and deeply nested URLs, which are less flexible and do not follow RESTful design principles as well
Part 4: Deep Nesting with Sub- Resources 
1. The Sub-Resource Locator Pattern
The Sub-Resource Locator pattern is a way to keep things modular and follow the Single Responsibility Principle. The application makes a clear distinction between "managing sensors" and "managing the history of a specific sensor" by having the SensorResource return an instance of SensorReadingResource for the {sensorId}/readings path. In a huge API, defining every deep path inside a single controller makes a "God class" that is too big and hard to maintain. Delegation separates the logic, makes the codebase easier to navigate, allows for cleaner unit testing, and lets developers securely encapsulate state (like the parent sensorId) within the lifecycle of the delegated sub-resource class.

Part 5: Advanced Error Handling, Exception Mapping & Logging
2. Dependency Validation (422 Unprocessable Entity) 
HTTP 404 is supposed to mean the URI isn't there at all. But in this situation, the URI does exist, like, say,/api/v1/sensors. And the client sends over a proper JSON payload too. The real issue comes from the data inside it, maybe a foreign key pointing to a room that doesn't exist. This means the issue is not with the endpoint, but with the semantics of the request. Hence, using HTTP 422 is the better choice here because the server receives the request and the syntax is correct, the media type checks out, but it can't process it because of those semantic problems. That gives a better hint to the client than just a 404 would, as 404 might confuse things, since the path is real. 422 points out the exact trouble spot without misleading.

4. The Global Safety Net (500) 
Exposing raw Java stack traces is a critical security vulnerability called Information Disclosure. The stack traces give you a very detailed map of the inner architecture of your application. An attacker can gather exact file paths, database schemas, internal IP addresses, exact frameworks, exact version numbers of libraries. The attacker can then use the version numbers of the libraries to check for known CVEs (Common Vulnerabilities and Exposures) and use zero-day exploits accordingly. Moreover, attackers can reverse engineer business logic to bypass validation constraints, as they can see exactly which line of code failed.

5. API Request & Response Logging Filters 
The principles of Aspect-Oriented Programming (AOP) are implemented using JAX-RS filters (ContainerRequestFilter and ContainerResponseFilter). Logging is a cross-cutting concern, a requirement that spans the entire application but is separate from the core business logic. Manually inserting Logger.info() in every endpoint is a violation of DRY (Don't Repeat Yourself) principle and results in very cluttered code. Filters pull this logic together into one class that automatically intercepts all inbound and outbound traffic, so you can achieve consistent and bulletproof logging across the whole API without touching a single line of the real resource methods.



        