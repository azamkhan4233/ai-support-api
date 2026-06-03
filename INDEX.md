# 📚 Project Index - Start Here!

## 👋 Welcome!

This is your complete **AI Customer Support API** project. Everything you need is here.

## 🎯 Start in This Order:

### 1️⃣ **First Time? Read This:**
📄 `WHAT_YOU_GOT.md` - Understand what you have (5 min read)

### 2️⃣ **Want to Run It Now?**
⚡ `QUICKSTART.md` - Get running in 5 minutes

### 3️⃣ **Need Detailed Setup?**
🔧 `SETUP_GUIDE.md` - Step-by-step instructions with troubleshooting

### 4️⃣ **Want to Understand the Code?**
🏗️ `PROJECT_STRUCTURE.md` - Complete code organization guide

### 5️⃣ **Ready to Use the API?**
📖 `README.md` - Full API documentation

## 📁 Project Files Guide

### 🚀 Getting Started Files
| File | Purpose | When to Use |
|------|---------|-------------|
| `QUICKSTART.md` | 5-minute setup | Want to run immediately |
| `SETUP_GUIDE.md` | Detailed setup | Need step-by-step help |
| `WHAT_YOU_GOT.md` | Project overview | First time opening project |

### 📚 Documentation Files
| File | Purpose | When to Use |
|------|---------|-------------|
| `README.md` | Main documentation | Learning about the API |
| `PROJECT_STRUCTURE.md` | Code organization | Understanding the codebase |
| `INDEX.md` | This file | Finding your way around |

### ⚙️ Configuration Files
| File | Purpose | When to Edit |
|------|---------|--------------|
| `pom.xml` | Maven dependencies | Adding libraries |
| `.env.example` | Environment template | Setting up environment |
| `application.yml` | Main config | Changing database/settings |
| `application-dev.yml` | Dev config | Development settings |

### 🧪 Testing Files
| File | Purpose | When to Use |
|------|---------|-------------|
| `postman_collection.json` | API tests | Testing endpoints |
| `data.sql` | Sample data | Initial database data |

### 🐳 Deployment Files
| File | Purpose | When to Use |
|------|---------|-------------|
| `docker/Dockerfile` | Container image | Building Docker image |
| `docker/docker-compose.yml` | Multi-container setup | Running with Docker |

## 🎓 Learning Path

### If You're New to Spring Boot:
1. Start with `PROJECT_STRUCTURE.md`
2. Read `SETUP_GUIDE.md`
3. Run the project with `QUICKSTART.md`
4. Explore code in this order:
   - Models (`model/` folder)
   - Repositories (`repository/` folder)
   - Services (`service/` folder)
   - Controllers (`controller/` folder)

### If You're Experienced:
1. Skim `WHAT_YOU_GOT.md`
2. Check `pom.xml` for dependencies
3. Review `application.yml` for config
4. Run with Docker: `cd docker && docker-compose up`
5. Test with Postman collection

### If You Want to Deploy:
1. Read deployment section in `SETUP_GUIDE.md`
2. Configure production settings in `application.yml`
3. Set up environment variables from `.env.example`
4. Build: `./mvnw clean package`
5. Deploy JAR or use Docker

## 🔍 Finding Specific Things

### "I want to change the AI behavior"
- Edit: `application.yml` → `openai.model`
- Or: Update Business profile via API
- See: `service/AIService.java` for logic

### "I want to add a new endpoint"
- Add: New method in appropriate controller
- Example: `controller/ChatController.java`
- Update: `postman_collection.json` with new test

### "I want to change database"
- Edit: `application.yml` → `spring.datasource.*`
- Or: Use H2 with `-Dspring-boot.run.profiles=dev`

### "I want to modify AI response logic"
- Edit: `service/AIService.java`
- Method: `generateResponse()`

### "I want to add new lead fields"
- Edit: `model/Lead.java`
- Update: `service/LeadService.java`
- Update: `dto/LeadDTO.java` (create if needed)

## 📋 Common Tasks

### Running the Application
```bash
# Quick start (H2 database)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# With Docker (PostgreSQL)
cd docker && docker-compose up -d

# Production mode
./mvnw spring-boot:run
```

### Testing the API
```bash
# Import to Postman
Open Postman → Import → postman_collection.json

# Or use curl (see QUICKSTART.md for examples)
```

### Building for Production
```bash
# Build JAR
./mvnw clean package

# Build Docker image
cd docker && docker build -t ai-support-api .
```

## 🆘 Need Help?

### Quick Fixes:
| Problem | Solution File |
|---------|---------------|
| Setup issues | `SETUP_GUIDE.md` → Troubleshooting section |
| API errors | `README.md` → API Documentation |
| Code questions | `PROJECT_STRUCTURE.md` → Component explanations |
| Docker issues | `docker/docker-compose.yml` → Check configuration |

### Still Stuck?
1. Check logs: `docker-compose logs -f app`
2. Verify environment: `.env` file is configured
3. Test endpoints: Use Postman collection
4. Check database: H2 console at `/h2-console`

## 🎯 Your Next Steps

**Absolute Beginner:**
1. ✅ Read `WHAT_YOU_GOT.md`
2. ✅ Follow `QUICKSTART.md`
3. ✅ Explore with Postman
4. ✅ Read `PROJECT_STRUCTURE.md`

**Ready to Deploy:**
1. ✅ Review `SETUP_GUIDE.md` deployment section
2. ✅ Configure production `.env`
3. ✅ Test with `postman_collection.json`
4. ✅ Deploy with Docker

**Want to Customize:**
1. ✅ Understand structure from `PROJECT_STRUCTURE.md`
2. ✅ Modify AI prompts in Business table
3. ✅ Add features to appropriate layers
4. ✅ Test with Postman

## 📂 Source Code Structure

```
src/main/java/com/aisupport/api/
├── AiSupportApiApplication.java    ← Start here
├── config/                          ← Configuration
├── controller/                      ← REST endpoints
├── service/                         ← Business logic
├── repository/                      ← Database access
├── model/                           ← Data entities
├── dto/                             ← Data transfer
├── security/                        ← JWT & auth
└── exception/                       ← Error handling
```

## 🌟 Pro Tips

1. **Start Simple**: Run with H2 first, then PostgreSQL
2. **Use Postman**: Import the collection for easy testing
3. **Check Logs**: They're your friend for debugging
4. **Read Errors**: Error messages are detailed and helpful
5. **Customize Gradually**: Get it running, then modify

## 🎁 Bonus Resources

- Swagger UI: `http://localhost:8080/swagger-ui.html` (when running)
- H2 Console: `http://localhost:8080/h2-console` (dev mode)
- Health Check: `http://localhost:8080/actuator/health`

## ✨ Quick Reference

**Default Credentials:**
- Email: `admin@techcorp.com`
- Password: `password123`

**Default Ports:**
- API: `8080`
- PostgreSQL: `5432`
- Redis: `6379`

**Environment Variables:**
- `OPENAI_API_KEY` - Your OpenAI key (required)
- `JWT_SECRET` - JWT signing key (required)

---

## 🚀 Ready to Start?

Pick your path:
- **⚡ Speed**: Go to `QUICKSTART.md`
- **📚 Learning**: Go to `WHAT_YOU_GOT.md`
- **🔧 Detailed**: Go to `SETUP_GUIDE.md`

**Good luck! You've got this! 🎉**
