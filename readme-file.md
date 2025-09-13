# Quarkus BCE Activiti Demo

A simple demonstration of **Boundary-Control-Entity (BCE)** architecture using **Quarkus** and **Activiti BPM** engine for job application workflow management.

## 🚀 Features

- **Quarkus Framework** - Fast startup and low memory footprint
- **Activiti BPM Engine** - In-memory workflow processing
- **BCE Architecture** - Clean separation of concerns
- **Bean Validation** - Input validation with proper error handling
- **Swagger UI** - Interactive API documentation
- **No Database Dependencies** - Pure in-memory storage
- **BPMN 2.0 Support** - Standard workflow notation

## 🔄 Workflow

Simple job applicant process following BPMN 2.0:

```
Start → HR Review → Engineering Manager Review → End
```

## 🏗️ Architecture

### Boundary Layer (REST API)
- `ActivityResource.java` - REST endpoints for workflow interaction

### Control Layer (Business Logic)
- `ActivityService.java` - Activiti engine integration and workflow management

### Entity Layer (Data Model)
- `JobApplicant.java` - Simple POJO with validation annotations

## 📋 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/job-applicants/start` | Start new job application process |
| `POST` | `/api/job-applicants/{id}/hr-complete` | Complete HR review task |
| `POST` | `/api/job-applicants/{id}/engineering-complete` | Complete engineering review task |
| `GET` | `/api/job-applicants/{id}` | Get applicant details by process ID |
| `GET` | `/api/job-applicants/tasks/hr` | Get pending HR tasks |
| `GET` | `/api/job-applicants/tasks/engineering` | Get pending engineering tasks |

## 🛠️ Prerequisites

- Java 17+
- Maven 3.8+

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd quarkus-bce-activiti-demo
```

### 2. Add BPMN File
Place your `handle-job-applicant.bpmn20.xml` file in `src/main/resources/`

### 3. Run the Application
```bash
./mvnw quarkus:dev
```

### 4. Access Swagger UI
Open your browser and navigate to:
```
http://localhost:8080/swagger-ui
```

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-openapi</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-hibernate-validator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-engine</artifactId>
        <version>7.1.0.M6</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 📝 Usage Examples

### Start a Job Application
```bash
curl -X POST http://localhost:8080/api/job-applicants/start \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "position": "Senior Developer"
  }'
```

**Response:**
```json
{
  "processId": "12345"
}
```

### Complete HR Review
```bash
curl -X POST http://localhost:8080/api/job-applicants/12345/hr-complete
```

### Get HR Tasks
```bash
curl http://localhost:8080/api/job-applicants/tasks/hr
```

### Complete Engineering Review
```bash
curl -X POST http://localhost:8080/api/job-applicants/12345/engineering-complete
```

## ✅ Input Validation

The API validates all input data:

**Valid Request:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "position": "Developer"
}
```

**Invalid Request:**
```json
{
  "userName": "John",
  "password": "123"
}
```

**Error Response:**
```json
{
  "error": "Name is required, Email is required, Position is required"
}
```

## 🔧 Configuration

### application.properties
```properties
quarkus.http.port=8080
quarkus.http.cors=true

# Swagger UI configuration
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui
```

## 🏃 Development Mode

Run in development mode with hot reload:
```bash
./mvnw quarkus:dev
```

## 📊 BPMN Process

The workflow follows this BPMN 2.0 process:

1. **Start Event** - Process begins
2. **HR Review Task** - Assigned to "hr" candidate group
3. **Engineering Manager Review Task** - Assigned to "engineering manager" candidate group  
4. **End Event** - Process completes

## 🧪 Testing

Use the Swagger UI to test all endpoints interactively:

1. Start the application in dev mode
2. Open `http://localhost:8080/swagger-ui`
3. Use "Try it out" buttons to test each endpoint
4. Observe the workflow progression through different states

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎯 Learning Objectives

This demo project demonstrates:

- **BCE Architecture Pattern** implementation
- **Activiti BPM** integration with Quarkus
- **REST API** design with proper validation
- **In-memory workflow** processing
- **BPMN 2.0** workflow modeling
- **Swagger/OpenAPI** documentation

Perfect for learning BPM workflows, clean architecture patterns, and modern Java development with Quarkus!