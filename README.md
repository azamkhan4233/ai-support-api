# AI Customer Support API

A production-ready Spring Boot REST API for AI-powered customer support with automatic lead extraction and management.

## Features

- 🤖 **AI-Powered Chat**: OpenAI-compatible API (Groq by default) for intelligent customer support
- 👤 **Customer Management**: Track and manage customer interactions
- 📊 **Lead Extraction**: Automatic lead generation from conversations
- 🔐 **JWT Authentication**: Secure API access with JWT tokens
- 💬 **Multi-tenant**: Support multiple businesses on a single platform
- 📈 **Analytics Ready**: Session tracking and conversation history
- 🐳 **Docker Support**: Easy deployment with Docker and Docker Compose

## Tech Stack

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Database**: PostgreSQL 15 (prod) / H2 (dev)
- **Security**: Spring Security + JWT
- **AI**: Groq or any OpenAI-compatible chat API
- **Containerization**: Docker

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 15+ (or use Docker)
- OpenAI API Key

### 1. Clone and Setup

```bash
git clone <your-repo-url>
cd ai-support-api
```

### 2. Configure Environment

Create a `.env` file in the project root:

```bash
OPENAI_API_KEY=your-openai-api-key
JWT_SECRET=your-secure-jwt-secret-at-least-32-characters
```

### 3. Run with Docker (Recommended)

```bash
cd docker
docker-compose up -d
```

The API will be available at `http://localhost:8080`

### 4. Run Locally (Without Docker)

```bash
# Start PostgreSQL and Redis locally first
# Then run:
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## API Endpoints

### Authentication

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

### Start Chat Session

```http
POST /api/chat/start
Content-Type: application/json

{
  "businessId": 1,
  "ipAddress": "192.168.1.1"
}
```

### Send Message

```http
POST /api/chat/{sessionId}/message
Content-Type: application/json

{
  "customerSessionId": "uuid-from-start-response",
  "content": "Hello, I need help with my order",
  "customerName": "John Doe",
  "customerEmail": "john@example.com"
}
```

### Get Leads

```http
GET /api/leads
Authorization: Bearer {your-jwt-token}
```

## Database Schema

The application uses the following main entities:

- **User**: System users (business employees)
- **Business**: Companies using the platform
- **Customer**: End customers chatting with AI
- **ChatSession**: Individual chat conversations
- **Message**: Chat messages
- **Lead**: Extracted leads from conversations

## Configuration

### application.yml

Main configuration file with database, JWT, and OpenAI settings.

### application-dev.yml / application-prod.yml

- **dev**: H2 in-memory database (default profile)
- **prod**: PostgreSQL for Docker/production

## Development

### Build the Project

```bash
./mvnw clean install
```

### Run Tests

```bash
./mvnw test
```

### Package

```bash
./mvnw package
```

## Deployment

### Docker Deployment

```bash
cd docker
docker-compose up -d
```

### AWS/Cloud Deployment

1. Build the JAR:
```bash
./mvnw clean package
```

2. Deploy the JAR file to your cloud provider

3. Set environment variables:
- `OPENAI_API_KEY`
- `JWT_SECRET`
- Database connection strings

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `OPENAI_API_KEY` | Your OpenAI API key | Yes |
| `JWT_SECRET` | Secret key for JWT tokens (32+ chars) | Yes |
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | Yes |
| `SPRING_DATASOURCE_USERNAME` | Database username | Yes |
| `SPRING_DATASOURCE_PASSWORD` | Database password | Yes |

## API Documentation

Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Monitoring

Health check endpoint:
```
http://localhost:8080/actuator/health
```

## Security Features

- ✅ JWT-based authentication
- ✅ Password encryption with BCrypt
- ✅ CORS configuration
- ✅ SQL injection prevention
- ✅ XSS protection

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please open a GitHub issue.

## Roadmap

- [ ] Email notifications
- [ ] Analytics dashboard
- [ ] Multi-language support
- [ ] Sentiment analysis
- [ ] Custom AI model training

---

Built with ❤️ using Spring Boot and OpenAI
