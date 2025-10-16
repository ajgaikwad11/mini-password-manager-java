# 🔒 Mini Password Manager with Secure Login + 2FA (Java)

A **console-based Java application** that demonstrates **secure login, hashed password storage, optional 2FA simulation, and credential management**.  
This project showcases core **Java, security, and OOP skills**.

---

## 🚀 Features

- **User Registration**: Securely register with hashed passwords + salt  
- **User Login**: Limits login attempts to 3 tries  
- **Simulated 2FA**: Optional 6-digit code verification during login  
- **Credential Storage**: Add and view multiple account credentials per user  
- **Password Security**: Passwords are hashed with SHA-256 + unique salt  
- Fully console-based for simplicity and easy learning  

---

## 🧩 Technologies Used

- **Language:** Java  
- **Libraries/APIs:**  
  - `java.security.MessageDigest` for hashing  
  - `java.security.SecureRandom` for salt generation  
  - `java.util.Base64` for encoding  
  - `java.util.Scanner` for console input  
  - `java.util.HashMap` & `java.util.Map` for in-memory storage  

---

## 💻 How It Works

1. **Registration**: User enters a username and password → password is hashed with a random salt → stored in memory.  
2. **Login**: User enters credentials → password is hashed with stored salt → compared with stored hash.  
3. **2FA Simulation**: A random 6-digit code is generated → user must enter it to complete login.  
4. **Credential Management**: Logged-in users can store multiple account passwords (hashed) and view them securely.  

---

## 📦 How to Run

### 1️⃣ Compile
```bash
javac MiniPasswordManager.java
```
### 2️⃣ Run
```bash
java MiniPasswordmanager
