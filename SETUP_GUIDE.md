# Setup Guide - AI Support API

## Complete Setup Instructions

### Step 1: Prerequisites

Ensure you have installed:
- ✅ Java 17 or higher
- ✅ Maven 3.6+ (or use included mvnw)
- ✅ Docker & Docker Compose (recommended)
- ✅ IntelliJ IDEA or Eclipse IDE
- ✅ PostgreSQL 15+ (if not using Docker)

### Step 2: Import Project into IDE

#### For IntelliJ IDEA:

1. Open IntelliJ IDEA
2. Click **File → Open**
3. Navigate to the `ai-support-api` folder
4. Select the `pom.xml` file
5. Click **Open as Project**
6. Wait for Maven to download dependencies (this may take a few minutes)

#### For Eclipse:

1. Open Eclipse
2. Click **File → Import → Maven → Existing Maven Projects**
3. Browse to the `ai-support-api` folder
4. Click **Finish**

### Step 3: Get OpenAI API Key

1. Go to https://platform.openai.com/
2. Sign up or log in
3. Navigate to **API Keys** section
4. Click **Create new secret key**
5. Copy the key (you won't be able to see it again!)

### Step 4: Configure Environment

#### Option A: Using .env file (Recommended)

1. Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```

2. Edit `.env` and add your OpenAI API key:
```
OPENAI_API_KEY=sk-your-actual-api-key-here
JWT_SECRET=my-super-secret-jwt-key-at-least-32-characters-long
```

#### Option B: Using application.yml

Edit `src/main/resources/application.yml`:
```yaml
openai:
  api:
    key: sk-your-actual-api-key-here
```

### Step 5: Choose Deployment Method

#### Method A: Docker (Easiest - Recommended)

1. Navigate to docker folder:
```bash
cd docker
```

2. Start all services:
```bash
docker-compose up -d
```

3. Check if services are running:
```bash
docker-compose ps
```

4. View logs:
```bash
docker-compose logs -f app
```

5. The API will be available at: `http://localhost:8080`

#### Method B: Local Development (H2 Database)

1. No database setup needed - uses H2 in-memory database

2. Run the application:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Access H2 Console at: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

#### Method C: Local with PostgreSQL

1. Install and start PostgreSQL

2. Create database:
```bash
createdb ai_support_db
```

3. Update `application.yml` with your database credentials

4. Run the application:
```bash
./mvnw spring-boot:run
```

### Step 6: Test the API

#### Option A: Using Postman

1. Import `postman_collection.json` into Postman
2. Test the login endpoint first
3. Copy the JWT token from the response
4. Use it in other requests

#### Option B: Using curl

1. Start a chat session:
```bash
curl -X POST http://localhost:8080/api/chat/start \
  -H "Content-Type: application/json" \
  -d '{"businessId": 1, "ipAddress": "127.0.0.1"}'
```

2. Send a message (replace {sessionId} with actual ID):
```bash
curl -X POST http://localhost:8080/api/chat/1/message \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello, I need help with my order",
    "customerName": "John Doe",
    "customerEmail": "john@example.com"
  }'
```

#### Option C: Using Swagger UI

1. Open browser: `http://localhost:8080/swagger-ui.html`
2. Explore and test all endpoints interactively

### Step 7: Initial Login Credentials

**Default Admin Account:**
- Email: `admin@techcorp.com`
- Password: `password123`

**Important:** Change this password in production!

### Step 8: Verify Everything Works

1. ✅ Check health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

2. ✅ Test login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@techcorp.com", "password": "password123"}'
```

3. ✅ Start a chat and send a message
4. ✅ Check if leads are being extracted

### Common Issues & Solutions

#### Issue: "Maven dependencies not downloading"
**Solution:**
```bash
./mvnw clean install -U
```

#### Issue: "Port 8080 already in use"
**Solution:** Change port in `application.yml`:
```yaml
server:
  port: 8081
```

#### Issue: "OpenAI API error"
**Solution:**
- Check your API key is correct
- Verify you have credits in your OpenAI account
- Check if the key is properly set in environment variables

#### Issue: "Database connection failed"
**Solution:**
- Verify PostgreSQL is running: `pg_isready`
- Check database credentials in `application.yml`
- Ensure database `ai_support_db` exists

#### Issue: "JWT token expired"
**Solution:** The token expires after 24 hours. Simply login again to get a new token.

### Development Workflow

1. Make code changes in your IDE
2. Application auto-reloads (Spring DevTools)
3. Test changes using Postman or curl
4. Check logs for any errors

### Stopping the Application

#### Docker:
```bash
cd docker
docker-compose down
```

#### Local:
Press `Ctrl + C` in the terminal

### Next Steps

1. ✅ Customize the AI prompt in Business settings
2. ✅ Add more users via database
3. ✅ Test with real customer scenarios
4. ✅ Monitor lead extraction
5. ✅ Deploy to production (AWS, DigitalOcean, etc.)

### Need Help?

- Check the README.md for API documentation
- Review the code comments
- Check Spring Boot logs
- Open an issue on GitHub

---

**You're all set! 🚀** Start building your AI-powered customer support system!
