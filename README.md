# Scalable URL Shortener

A production-ready, microservices-based URL shortener built with Java and Spring Boot. Designed for high availability and 
horizontal scalability, this system handles URL shortening, user management, real-time notifications, click analytics, and 
redirect caching — all through a distributed, event-driven architecture.

---

## Table of Contents

- [Overview](#overview)
- [Services](#services)
- [Tech Stack](#tech-stack)
- [API Overview](#api-overview)
- [Features](#features)

---

## Overview

This project implements a URL shortener as a set of loosely coupled microservices. Each service owns a specific bounded 
context and communicates either synchronously via OpenFeign or asynchronously via Apache Kafka. A React.js frontend 
interacts with the backend through a central API Gateway. All services register with Eureka, pull their configuration 
from a central config server, and are protected by JWT-based authentication, circuit breakers, and rate limiting.

---

## Services

| Service | Responsibility |
|---|---|
| **discovery-server** | Eureka service registry. All microservices register here for dynamic service resolution. |
| **config-server** | Spring Cloud Config Server providing centralized, version-controlled configuration to all services. |
| **user-service** | User registration, 6-digit OTP generation (Redis TTL), email verification, JWT login, and admin operations. Publishes registration events to Kafka. Exposes Feign-compatible endpoints for URL count management. |
| **url-service** | Core service for URL creation, redirect, and deletion. Uses Redis as a cache layer for redirect lookups. Calls user-service via Feign to keep URL count in sync. Publishes click events to Kafka for analytics. |
| **notification-service** | Kafka consumer that sends transactional emails — OTP delivery on registration, welcome messages on verification. |
| **analytics-service** | Kafka consumer that records click events (timestamp, IP, device, browser, country) and exposes stats endpoints for the frontend dashboard. |
| **common-lib** | Shared library with reusable DTOs, custom exception classes, and event payload definitions used across services. |

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot, Spring Cloud |
| Frontend | React.js |
| Service Discovery | Netflix Eureka |
| Inter-Service Calls | OpenFeign |
| Messaging | Apache Kafka |
| Caching | Redis |
| Authentication | JWT (stored in HttpOnly cookie, 1hr expiry) |
| Email | JavaMail (SMTP) |
| Config Management | Spring Cloud Config |
| Resilience | Resilience4j (Circuit Breaker) |
| Rate Limiting | Spring Cloud Gateway / Bucket4j |
| Build Tool | Maven (multi-module) |
| Architecture | Event-driven Microservices |

---

## API Overview

### User Service

| Method | Endpoint                        | Auth             | Description                                     |
|--------|---------------------------------|------------------|-------------------------------------------------|
| `POST` | `/api/users/register`           | Public           | Register a new account. Triggers OTP email.     |
| `POST` | `/api/users/verify-otp`         | Public           | Submit the 6-digit OTP to activate the account. |
| `POST` | `/api/users/login`              | Public           | Authenticate and receive a JWT cookie (1hr).    |
| `POST` | `/api/users/logout`             | JWT              | Invalidate the current session.                 |
| `GET`  | `/api/admin/users`              | JWT + Admin      | Paginated list of all users.                    |
| `POST` | `/api/users/{id}/increment-url` | Internal (Feign) | Increment a user's URL count.                   |
| `POST` | `/api/users/{id}/decrement-url` | Internal (Feign) | Decrement a user's URL count.                   |

### URL Service

| Method   | Endpoint            | Auth   | Description                                        |
|----------|---------------------|--------|----------------------------------------------------|
| `POST`   | `/api/urls/shorten` | JWT    | Create a new short URL.                            |
| `GET`    | `/{shortCode}`      | Public | Redirect to the original URL (cache-first).        |
| `GET`    | `/api/urls`         | JWT    | List all URLs belonging to the authenticated user. |
| `DELETE` | `/api/urls/{id}`    | JWT    | Delete a short URL and evict its cache entry.      |

### Analytics Service

| Method | Endpoint                     | Auth | Description                                                   |
|--------|------------------------------|------|---------------------------------------------------------------|
| `GET`  | `/api/analytics/{shortCode}` | JWT  | Total clicks, devices, browsers, and top countries for a URL. |
| `GET`  | `/api/analytics/dashboard`   | JWT  | Aggregated stats for all URLs owned by the user.              |

---

## Features

- **URL Shortening** — Generate unique short codes; supports optional custom aliases
- **Cache-First Redirect** — Redis lookup before database; dramatically reduces redirect latency
- **Click Analytics** — Tracks every redirect with IP, device, browser, and timestamp via Kafka
- **OTP Registration Flow** — 6-digit OTP generated, stored in Redis with TTL, delivered via email
- **JWT Authentication** — Stateless auth via HttpOnly cookies with 1-hour expiry
- **Feign-Based URL Count Sync** — url-service calls user-service on create/delete to keep counts accurate
- **Circuit Breaker** — Resilience4j protects Feign calls; graceful fallbacks on downstream failures
- **Rate Limiting** — API Gateway enforces per-user and per-IP request limits
- **Event-Driven Notifications** — Kafka decouples email sending from user registration logic
- **Centralized Configuration** — Spring Cloud Config manages all service properties in one place
- **Service Discovery** — Eureka enables zero-hardcoded-URL service resolution
- **Admin Dashboard** — Paginated user listing with custom DTOs for admin operations
- **React.js Frontend** — Full user interface for registration, login, URL management, and analytics

---