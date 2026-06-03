# Project Structure

```
ai-support-api/
│
├── src/
│   ├── main/
│   │   ├── java/com/aisupport/api/
│   │   │   ├── AiSupportApiApplication.java          # Main Spring Boot application
│   │   │   │
│   │   │   ├── config/                               # Configuration classes
│   │   │   │   ├── SecurityConfig.java              # JWT & security config
│   │   │   │   └── OpenAIConfig.java                # OpenAI client config
│   │   │   │
│   │   │   ├── controller/                          # REST API endpoints
│   │   │   │   ├── AuthController.java              # Login, registration
│   │   │   │   ├── ChatController.java              # Chat operations
│   │   │   │   ├── LeadController.java              # Lead management
│   │   │   │   └── BusinessController.java          # Business profile
│   │   │   │
│   │   │   ├── service/                             # Business logic
│   │   │   │   ├── AuthService.java                 # Authentication logic
│   │   │   │   ├── ChatService.java                 # Chat management
│   │   │   │   ├── AIService.java                   # OpenAI integration
│   │   │   │   └── LeadService.java                 # Lead extraction
│   │   │   │
│   │   │   ├── repository/                          # Database access
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── BusinessRepository.java
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── ChatSessionRepository.java
│   │   │   │   ├── MessageRepository.java
│   │   │   │   └── LeadRepository.java
│   │   │   │
│   │   │   ├── model/                               # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Business.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── ChatSession.java
│   │   │   │   ├── Message.java
│   │   │   │   └── Lead.java
│   │   │   │
│   │   │   ├── dto/                                 # Data transfer objects
│   │   │   │   ├── ChatRequestDTO.java
│   │   │   │   ├── ChatResponseDTO.java
│   │   │   │   ├── SessionStartDTO.java
│   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   └── JwtResponseDTO.java
│   │   │   │
│   │   │   ├── security/                            # Security components
│   │   │   │   ├── JwtTokenProvider.java            # JWT generation/validation
│   │   │   │   ├── JwtAuthenticationFilter.java    # Filter for JWT
│   │   │   │   └── UserDetailsServiceImpl.java     # User loading
│   │   │   │
│   │   │   └── exception/                           # Exception handling
│   │   │       ├── ResourceNotFoundException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml                      # Main configuration
│   │       ├── application-dev.yml                  # Dev profile (H2)
│   │       └── data.sql                             # Sample data
│   │
│   └── test/
│       └── java/com/aisupport/api/
│           └── (test classes go here)
│
├── docker/
│   ├── Dockerfile                                   # Application container
│   └── docker-compose.yml                           # Multi-container setup
│
├── .mvn/                                            # Maven wrapper
│   └── wrapper/
│
├── pom.xml                                          # Maven dependencies
├── README.md                                        # Project documentation
├── SETUP_GUIDE.md                                   # Setup instructions
├── PROJECT_STRUCTURE.md                             # This file
├── .gitignore                                       # Git ignore rules
├── .env.example                                     # Environment template
└── postman_collection.json                          # API testing collection
```

## Key Components

### Controllers (REST API Layer)
- **AuthController**: Handles login and user authentication
- **ChatController**: Manages chat sessions and messages
- **LeadController**: CRUD operations for leads
- **BusinessController**: Business profile management

### Services (Business Logic Layer)
- **AIService**: Integrates with OpenAI API for chat responses
- **ChatService**: Manages chat sessions and message flow
- **LeadService**: Extracts and manages leads from conversations
- **AuthService**: Authentication and user management

### Repositories (Data Access Layer)
- JPA repositories for database operations
- Each entity has its own repository interface

### Models (Database Entities)
- **User**: System users (employees of businesses)
- **Business**: Companies using the platform
- **Customer**: End customers chatting with AI
- **ChatSession**: Individual chat conversations
- **Message**: Individual messages in chats
- **Lead**: Extracted leads from conversations

### Security
- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control ready

### Configuration
- **SecurityConfig**: Spring Security + JWT setup
- **OpenAIConfig**: HTTP client for OpenAI API

## Data Flow

```
Customer Request
    ↓
ChatController
    ↓
ChatService ← → AIService (OpenAI)
    ↓
MessageRepository (Save)
    ↓
LeadService (Extract info)
    ↓
LeadRepository (Save)
    ↓
Response to Customer
```

## API Endpoints Summary

### Public Endpoints (No Auth)
- POST `/api/auth/login` - User login
- POST `/api/chat/start` - Start chat session
- POST `/api/chat/{id}/message` - Send message
- GET `/api/chat/{id}/history` - Get history

### Protected Endpoints (JWT Required)
- GET `/api/auth/me` - Get current user
- GET `/api/business/profile` - Get business profile
- PUT `/api/business/profile` - Update business
- GET `/api/leads` - List leads
- PUT `/api/leads/{id}/status` - Update lead

## Database Schema

```
users
├── id (PK)
├── email (unique)
├── name
├── password (BCrypt)
├── role
├── business_id (FK)
└── created_at

businesses
├── id (PK)
├── name
├── description
├── industry
├── contact_email
├── agent_system_prompt
└── created_at

customers
├── id (PK)
├── business_id (FK)
├── session_id (unique)
├── ip_address
├── name
├── email
└── created_at

chat_sessions
├── id (PK)
├── business_id (FK)
├── customer_id (FK)
├── started_at
├── ended_at
└── status

messages
├── id (PK)
├── session_id (FK)
├── sender (user/assistant)
├── content
└── timestamp

leads
├── id (PK)
├── business_id (FK)
├── name
├── email
├── phone
├── interest
├── status
└── created_at
```

## Technology Stack

- **Backend**: Spring Boot 3.2.1
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL 15 / H2 (dev)
- **Cache**: Redis 7
- **AI**: OpenAI GPT-4
- **Build**: Maven
- **Container**: Docker
- **API Docs**: Swagger/OpenAPI

## Environment Profiles

- **dev**: H2 in-memory database, for development
- **prod**: PostgreSQL, for production
- **test**: H2, for testing

## Deployment Options

1. **Docker Compose** (Recommended for local/testing)
2. **AWS Elastic Beanstalk**
3. **Heroku**
4. **DigitalOcean App Platform**
5. **Google Cloud Run**
6. **Traditional VPS** (DigitalOcean, Linode, etc.)
