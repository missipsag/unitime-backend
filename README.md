# UniTime Backend – University Calendar & Scheduling Platform

A comprehensive REST API backend built with Spring Boot 3.5.5 and PostgreSQL that powers a mobile calendar application designed to streamline university schedule communications and event management for students and administrators.

## Overview

UniTime solves a critical communication gap in universities across Algeria and beyond. Faculty and administrators often publish sudden schedule changes and campus events on universities' official websites and Google Classroom, which students rarely check consistently. UniTime provides a dedicated platform with a sleek calendar interface (using SfCalendar) and real-time notifications to ensure students never miss important updates about their courses, exams, and campus events.

## Key Features

### User Management & Authentication
- **Role-Based Access Control**: Support for STUDENT, GROUP_ADMIN, SECTION_ADMIN, and SUPER_ADMIN roles
- **JWT Token Authentication**: Secure user registration, login, and password encryption using BCrypt
- **Hierarchical User Structure**: Users belong to Groups within Promotions (academic levels)

### Academic Organization
- **Promotions**: Academic levels (L1, L2, L3, M1, M2, D1, D2, D3) organized by field of study
- **Groups**: Specific class cohorts within promotions with unique access codes for enrollment
- **Access Codes**: Secure enrollment mechanism preventing unauthorized group/promotion access

### Appointment & Calendar Management
- **Multiple Event Types**: COURSE, EXAM, TD (Tutorial), TP (Practical), TEST, SPECIAL_EVENT
- **Recurring Events**: Full support for RFC 5545 RRULE format (DAILY and WEEKLY patterns)
- **Scope-Based Visibility**: Events can be visible at GROUP or PROMOTION level
- **Intelligent Filtering**: Real-time appointment retrieval based on current date/time and recurrence rules

### Data Integrity & Error Handling
- **Comprehensive Exception Handling**: Custom error responses for 10+ exception types
- **Database Constraints**: Unique constraints, foreign key relationships, and check constraints
- **Transaction Management**: Automatic timestamp updates and data consistency

## Technical Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.5
- **Build Tool**: Gradle
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA & Hibernate
- **Security**: Spring Security with JWT (JJWT 0.12.3)
- **Mapping**: MapStruct 1.5.5
- **Utilities**: Lombok
- **Validation**: Jakarta Validation
- **Testing**: JUnit 5, Spring Security Test

## Project Structure

```
src/main/java/com/unitime/unitime_backend/
├── UnitimeBackendApplication.java
├── config/
│   ├── ApplicationConfiguration.java    # Bean configurations
│   ├── JwtAuthenticationFilter.java     # JWT validation filter
│   └── SecurityConfig.java              # Security & CORS setup
├── controller/                          # REST endpoints
│   ├── AppointmentController.java
│   ├── AuthenticationController.java
│   ├── GroupController.java
│   ├── PromotionController.java
│   └── UserController.java
├── service/                             # Business logic
│   ├── AppointmentService.java
│   ├── AuthenticationService.java
│   ├── GroupService.java
│   ├── JwtService.java
│   ├── PromotionService.java
│   └── UserService.java
├── entity/                              # Domain models
│   ├── User.java
│   ├── Group.java
│   ├── Promotion.java
│   ├── Appointment.java
│   ├── UserRole.java (enum)
│   ├── AppointmentType.java (enum)
│   ├── PromotionLevel.java (enum)
│   ├── AppointmentScope.java (enum)
│   └── AppointmentVisibility.java
├── repository/                          # Data access layer
│   ├── AppointmentRepository.java
│   ├── GroupRepository.java
│   ├── PromotionRepository.java
│   └── UserRepository.java
├── mapper/                              # DTO mapping
│   ├── AppointmentMapper.java
│   ├── GroupMapper.java
│   ├── PromotionMapper.java
│   └── UserMapper.java
├── dto/                                 # Data transfer objects
│   ├── appointment/
│   ├── group/
│   ├── promotion/
│   ├── user/
│   ├── jwt/
│   └── error/
├── exception/                           # Custom exceptions
│   ├── BaseException.java
│   ├── DatabaseException.java
│   ├── DuplicateResourceException.java
│   ├── ForbiddenException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── ValidationException.java
└── handler/
    └── GlobalExceptionHandler.java      # Global exception advice
```

## Database Schema

### Core Tables
- **users**: User account information with role and group/promotion assignments
- **promotions**: Academic levels (L1-D3) organized by field of study
- **groups**: Specific class cohorts within promotions
- **appointments**: Calendar events with recurrence support
- **appointment_visibility**: Junction table for appointment scope (GROUP or PROMOTION)
- **user_groups**: User enrollment in groups

### Features
- Automatic timestamp management via triggers
- Comprehensive indexing for query performance
- Constraints ensuring data integrity (unique combinations, foreign keys, checks)
- Support for 30+ sample appointments with various recurrence patterns

See [unitime_db.sql](unitime_db.sql) for full database schema.

## Authentication Flow

1. User registers or logs in via `/api/auth/register` or `/api/auth/login`
2. Server validates credentials and returns JWT token with expiration
3. Client includes token in `Authorization: Bearer <token>` header
4. `JwtAuthenticationFilter` intercepts requests and validates token
5. Authenticated user context available via `SecurityContextHolder`

## Security Configuration

- **Stateless Session Management**: No server-side sessions, purely token-based
- **CORS Policy**: Configured endpoints and allowed methods for frontend integration
- **Password Encoding**: BCrypt with variable iterations
- **JWT Secret**: Environment variable based (configurable)
- **Token Expiration**: Configurable expiration time

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL 12+
- Gradle (or use gradlew wrapper)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd unitime-backend
   ```

2. **Configure environment variables**
   Create `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/unitime_db
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=validate
   jwt.secret=your-secret-key-min-256-bits
   jwt.expiration=86400000
   ```

3. **Initialize the database**
   ```bash
   psql -U postgres -d unitime_db -f unitime_db.sql
   ```

4. **Build and run**
   ```bash
   ./gradlew bootRun
   ```

The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

### Users
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile
- `DELETE /api/users/{id}` - Delete user account

### Promotions
- `POST /api/promotions` - Create promotion (admin)
- `GET /api/promotions/{accessCode}` - Get promotion details
- `PUT /api/promotions/{accessCode}` - Update promotion (admin)
- `DELETE /api/promotions/{accessCode}` - Delete promotion (admin)

### Groups
- `POST /api/groups` - Create group (admin)
- `GET /api/groups/{accessCode}` - Get group details
- `PUT /api/groups/{accessCode}` - Update group (admin)
- `DELETE /api/groups/{accessCode}` - Delete group (admin)
- `POST /api/groups/join` - Join group using access code (student)

### Appointments
- `POST /api/appointments` - Create appointment (admin)
- `GET /api/appointments` - List all relevant appointments
- `GET /api/appointments/current` - Get currently occurring appointments
- `DELETE /api/appointments/{id}` - Delete appointment (admin)

## Architecture Highlights

### Design Patterns Implemented
- **Repository Pattern**: Data access abstraction layer
- **Service Layer Pattern**: Business logic separation
- **DTO Pattern**: Clean API contracts between client and server
- **Dependency Injection**: Loose coupling via Spring IoC
- **Global Exception Handler**: Centralized error management
- **JWT Token Pattern**: Stateless authentication

### Security Best Practices
- Password hashing with BCrypt
- JWT tokens with expiration
- Role-based authorization checks
- CORS policy enforcement
- Input validation and constraint handling
- SQL injection prevention via Hibernate ORM

### Performance Optimizations
- Database indexes on frequently queried columns
- Lazy loading configuration for relationships
- Stream API for efficient collection processing
- Connection pooling via Spring Boot defaults

## Development Notes

- All services use `@Transactional` annotation for ACID compliance
- Sensitive information (JWT secret, DB credentials) managed via environment variables
- MapStruct used for compile-time type-safe mapping
- Custom exceptions extend `BaseException` for consistent error handling
- Appointment filtering supports DAILY, WEEKLY recurrence rules

## Future Enhancements

- Email notifications for schedule changes
- Integration with university official systems
- Advanced recurring event patterns (MONTHLY, YEARLY)
- Appointment attendance tracking
- Conflict detection and warnings
- Calendar sync with iCal format
- Mobile push notifications
- Analytics dashboard for administrators

## License

This project is developed as part of learning mobile app development with Flutter.

---

For questions or contributions, please contact the project maintainer.
