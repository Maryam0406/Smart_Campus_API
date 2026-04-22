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


        