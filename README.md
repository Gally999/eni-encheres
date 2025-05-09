# 🛒 ENI ENCHERES – Application Spring Boot

Une application d'enchères développée avec **Java 17**, **Spring Boot 3.4.3**, **Thymeleaf** et **SQL Server** par **Cécile Daguin** et **Laëtitia Petit**.

---

## 🚀 Technologies

- **Java 17**
- **Spring Boot 3.4.3**
    - Spring Web
    - Spring Data JPA
    - Spring Security
- **Thymeleaf**
- **SQL Server**
- **Gradle**

---

## ⚙️ Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/Gally999/Encheres.git
cd Encheres
```

### 2. Configurer la base de données
Dans le fichier `application.properties` :
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databasename=ENCHERES_DB;integratedSecurity=false;encrypt=false;trustServerCertificate=false
spring.datasource.username=your_username
spring.datasource.password=your_password
```
>💡 Assure-toi que SQL Server est bien lancé et que le port 1433 est ouvert.

### 3. Lancer l'application
- Avec Gradle
```bash 
./gradlew bootRun
```
> Ou directement via ton IDE (IntelliJ / Eclipse).

### 🌐 Accès à l'application
Une fois l'application lancée :
```navigateur
http://localhost:8080/
```

### 🔐 Authentification
L'application utilise Spring Security. Un système de connexion avec rôles et gestion des utilisateurs est présent.

### 🧪 Fonctionnalités principales
- [X] Inscription / Connexion utilisateur
- [X] Affichage / Modification / Suppression profil
- [X] Filtrer les enchères
- [X] Création / Annulation / Affichage détails d'enchères
- [X] Pages dynamiques avec Thymeleaf

### 🧰 Outils utiles
- [Spring Initializr](https://start.spring.io/)
- [SQL Server Management Studio (SSMS)](https://learn.microsoft.com/en-us/ssms/download-sql-server-management-studio-ssms)
- [How to Install SQL Server Management Studio on a Mac](https://builtin.com/software-engineering-perspectives/sql-server-management-studio-mac)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Spring Security Docs](https://docs.spring.io/spring-security/reference/)

### 📄 Licence
Projet académique à visée pédagogique. Toute utilisation commerciale est interdite sans autorisation.
