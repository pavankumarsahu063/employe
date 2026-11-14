# Employee Management REST API

A Spring Boot 3 REST API for managing employees, ready for local execution and deployment on Render.

---

## 🚀 Available Endpoints

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Root endpoint showing API summary & endpoints |
| `GET` | `/health` | Health check endpoint (used by Render) |
| `GET` | `/api/employees` | List all employees |
| `GET` | `/api/employees/{id}` | Get employee details by ID |
| `POST` | `/api/employees` | Add a new employee |
| `PUT` | `/api/employees/{id}` | Update an existing employee |
| `DELETE` | `/api/employees/{id}` | Delete an employee by ID |
| `GET` | `/api/employees/department/{dept}` | Filter employees by department name |

---

## 📋 Employee JSON Format

```json
{
  "firstName": "Alex",
  "lastName": "Morgan",
  "email": "alex.morgan@example.com",
  "department": "Engineering",
  "role": "Full Stack Developer",
  "salary": 92000.0
}
```

---

## 💻 Local Execution

### Run locally with Maven:
```bash
mvn spring-boot:run
```

### Run tests:
```bash
mvn test
```

### Build JAR:
```bash
mvn clean package -DskipTests
java -jar target/employe-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Deploy to Render

1. **Initialize Git & Push to GitHub**:
   ```bash
   git init
   git add .
   git commit -m "Add Employee APIs and Render deployment configuration"
   git branch -M main
   git remote add origin https://github.com/<your-username>/<your-repo-name>.git
   git push -u origin main
   ```

2. **Deploy on Render ([render.com](https://render.com))**:
   - Go to [dashboard.render.com](https://dashboard.render.com/) and click **New +** -> **Web Service**.
   - Connect your GitHub repository.
   - Choose **Docker** as the Runtime (it will automatically use the provided `Dockerfile`).
   - Select the **Free** tier.
   - Click **Create Web Service**.
   - Once deployed, your live API URL will be available (e.g., `https://employee-service.onrender.com`).
