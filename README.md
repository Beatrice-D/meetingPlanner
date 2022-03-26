# meetingPlanner
## How to use?

```
> To build project, run clean install with maven command line
> And deploy on localhost:8080
```

## Features

* SpringBoot REST API
* TestContainers
* JUnit 4

## API Controller
Meeting Planner Controller :
* The handleMeetingPlanner controller service is to manage the scheduling of meetings in rooms according to the following characteristics:
number of people, type of meeting, available resources and schedule
* Signature:
@return returns all associations between rooms and meetings
public ResponseEntity<Map<String, List<Reunion>>> handleMeetingPlanner();

```
Run
> http://localhost:8080/handle-meeting-planner
on your navigator to execute the controllers
```

## Tests with mockMVC
* There is one test service as follows:
* handleMeetingPlanner_shouldReturn200 to test controller service public ResponseEntity<Map<String, List<Reunion>>> handleMeetingPlanner();

