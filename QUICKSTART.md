# Quick Start - AI Support API

Get your AI customer support system running in under 5 minutes!

## ⚡ Super Fast Setup

### 1. Prerequisites Check
```bash
# Check Java version (need 17+)
java -version

# Check Maven (or you can use included ./mvnw)
mvn -version

# Check Docker (optional but recommended)
docker --version
```

### 2. Get Your OpenAI API Key
1. Visit: https://platform.openai.com/api-keys
2. Create new key
3. Copy it

### 3. Configure
```bash
# Create .env file
cp .env.example .env

# Edit .env and paste your OpenAI key
OPENAI_API_KEY=sk-your-actual-key-here
JWT_SECRET=my-super-secret-jwt-key-change-this-please
```

### 4. Run (Choose ONE method)

#### Option A: Docker (Easiest) ⭐
```bash
cd docker
docker-compose up -d
```
✅ Done! API running at http://localhost:8080

#### Option B: Local with H2 Database
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
✅ Done! API running at http://localhost:8080

### 5. Test It!

#### Test 1: Start a chat
```bash
curl -X POST http://localhost:8080/api/chat/start \
  -H "Content-Type: application/json" \
  -d '{"businessId": 1, "ipAddress": "127.0.0.1"}'
```

You'll get response like:
```json
{
  "sessionId": 1,
  "customerSessionId": "uuid-here",
  "status": "ACTIVE"
}
```

#### Test 2: Send a message (replace sessionId with actual ID)
```bash
curl -X POST http://localhost:8080/api/chat/1/message \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hi, I need help with pricing",
    "customerName": "John Doe",
    "customerEmail": "john@example.com"
  }'
```

You'll get AI response! 🎉

#### Test 3: Login as admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@techcorp.com",
    "password": "password123"
  }'
```

Copy the JWT token from response.

#### Test 4: Check leads (use JWT from above)
```bash
curl http://localhost:8080/api/leads \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🎯 What You Just Built

✅ Multi-tenant AI chatbot API
✅ Automatic lead extraction
✅ JWT authentication
✅ Customer management
✅ Chat history
✅ PostgreSQL or H2 database
✅ Docker ready

## 📚 Next Steps

1. **Explore the API**
   - Import `postman_collection.json` into Postman
   - Or visit http://localhost:8080/swagger-ui.html

2. **Customize AI Behavior**
   - Login and update Business profile
   - Modify `agentSystemPrompt` field

3. **Add More Users**
   - Create users via database
   - Assign roles (ADMIN, USER, MANAGER)

4. **Deploy to Production**
   - See SETUP_GUIDE.md for deployment options
   - Don't forget to change default password!

5. **Build Frontend**
   - Use the chat widget template
   - Or build your own React/Vue/Angular app

## 🆘 Troubleshooting

**Problem**: Port 8080 already in use
```bash
# Change port in application.yml
server:
  port: 8081
```

**Problem**: OpenAI API error
- Check your API key is correct
- Verify you have credits
- Check key is in .env or application.yml

**Problem**: Database error
- If using Docker: `docker-compose down && docker-compose up -d`
- If local: Check PostgreSQL is running

**Problem**: Maven dependencies not downloading
```bash
./mvnw clean install -U
```

## 📖 Documentation

- `README.md` - Full project documentation
- `SETUP_GUIDE.md` - Detailed setup instructions
- `PROJECT_STRUCTURE.md` - Code organization
- API Docs - http://localhost:8080/swagger-ui.html

## 💡 Pro Tips

1. **Use Docker** for easiest setup
2. **Use Postman** collection for testing
3. **Monitor logs** for debugging
4. **Check H2 console** at http://localhost:8080/h2-console (dev mode)
5. **Customize AI prompts** per business

## 🚀 You're Ready!

Your AI customer support system is running. Start chatting and watch it automatically extract leads!

**Default Login:**
- Email: admin@techcorp.com
- Password: password123

**Change this in production!**

---

Questions? Check SETUP_GUIDE.md or open an issue.
