# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

ziPOS Mobile is a comprehensive Point of Sale (POS) application built with modern Android development practices. This is a complete production-ready retail management system with inventory, customer management, analytics, and offline capabilities.

## Technology Stack & Architecture

- **Language**: Kotlin
- **UI Framework**: 100% Jetpack Compose with Material Design 3 
- **Architecture**: MVVM with Repository Pattern
- **Database**: Room (SQLite) with TypeConverters
- **Dependency Injection**: Hilt
- **Navigation**: Compose Navigation + Navigation Component
- **State Management**: StateFlow + collectAsStateWithLifecycle
- **Async**: Kotlin Coroutines with structured concurrency

## Key Build Commands

### Development Commands
```bash
# Build debug version
./gradlew app:assembleDebug

# Build release version  
./gradlew app:assembleRelease

# Run tests
./gradlew test
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Install debug APK on connected device
./gradlew installDebug

# Generate lint report
./gradlew lint

# Check for dependency updates
./gradlew dependencyUpdates
```

### Testing Commands
```bash
# Run unit tests only
./gradlew app:testDebugUnitTest

# Run instrumented tests
./gradlew app:connectedDebugAndroidTest

# Run specific test class
./gradlew test --tests com.mopanesystems.ziposmobile.ExampleUnitTest

# Generate test coverage report
./gradlew jacocoTestReport
```

## Code Architecture

### Package Structure
```
com.mopanesystems.ziposmobile/
├── ui/                     # All UI-related code (100% Compose)
│   ├── pos/               # Point of Sale screen & logic
│   ├── products/          # Product management
│   ├── customers/         # Customer management  
│   ├── inventory/         # Inventory & stock management
│   ├── analytics/         # Reports & analytics
│   ├── barcode/           # Barcode scanning integration
│   ├── offline/           # Offline mode handling
│   ├── navigation/        # Compose navigation setup
│   └── theme/             # Material Design 3 theming
├── data/                  # Data layer
│   ├── database/          # Room database & DAOs
│   ├── repository/        # Repository implementations
│   └── model/             # Data models & entities
├── di/                    # Hilt dependency injection
└── services/              # Background services & sync
```

### Key Patterns

**MVVM Implementation:**
- Each screen has a corresponding ViewModel that exposes StateFlow
- UI observes state using `collectAsStateWithLifecycle()`
- ViewModels interact with repositories, never directly with DAOs

**Repository Pattern:**
- Centralized data access layer between ViewModels and data sources
- Handles data synchronization between local Room database and potential remote APIs
- Implements caching strategies and offline-first approach

**Database Design:**
- Comprehensive Room database with 17+ entities covering POS, inventory, analytics
- TypeConverters for complex data types (lists, dates, enums)
- DAOs provide reactive queries using Flow for real-time UI updates

## Important Implementation Details

### Jetpack Compose Migration
This project has been **fully migrated from XML to Jetpack Compose**:
- All user-facing screens use Compose with Material Design 3
- Only ZXing barcode scanner retains XML layouts (library requirement)
- Navigation uses Compose Navigation with bottom navigation
- State management leverages Compose's reactive model

### Database Entities
Key entities in the Room database:
- `Product`, `Category`, `Subcategory` - Product catalog
- `Customer`, `CustomerTransaction` - Customer management  
- `Transaction`, `TransactionItem`, `Payment` - Sales processing
- `InventoryAdjustment`, `StockAlert` - Inventory management
- `DailySales`, `ProductPerformance`, `CustomerAnalytics` - Reporting
- `Store`, `User`, `UserPermission` - Multi-store & role management

### Feature Modules
- **POS System**: Complete cart management with multiple payment methods
- **Inventory Management**: Stock tracking, adjustments, supplier management, purchase orders  
- **Customer Management**: Profiles, transaction history, loyalty points, risk analysis
- **Analytics**: Sales reports, KPIs, customer insights with MPAndroidChart integration
- **Barcode Scanning**: ZXing integration with camera permissions
- **Receipt Generation**: PDF generation using iText library
- **Offline Support**: Queue operations with WorkManager synchronization

## Development Guidelines

### When Adding New Features
1. Follow MVVM pattern: Screen (Compose) → ViewModel → Repository → DAO
2. Use StateFlow for reactive state management in ViewModels
3. Implement proper error handling and loading states
4. Add appropriate Room entities/DAOs if new data is involved
5. Consider offline scenarios and queue operations
6. Use Hilt for dependency injection

### UI Development  
- Use Compose with Material Design 3 components
- Follow the established theming in `ui/theme/`
- Implement proper accessibility features
- Use `collectAsStateWithLifecycle()` for state observation
- Handle loading, error, and empty states consistently

### Database Changes
- Add migrations when modifying Room schema
- Update relevant DAOs and repositories  
- Consider data integrity and foreign key relationships
- Test migrations thoroughly

### Testing Approach
- Unit tests for ViewModels and Repositories using kotlinx-coroutines-test
- Room database tests with in-memory database
- Compose UI tests for screen interactions
- Integration tests for end-to-end workflows

## Key Dependencies

This project uses version catalogs (`libs.versions.toml`) for dependency management. Key libraries:
- Jetpack Compose BOM for UI framework
- Room for local database with KSP processing
- Hilt for dependency injection
- Navigation Component for screen navigation  
- ZXing for barcode scanning
- MPAndroidChart for analytics visualization
- iText for PDF receipt generation
- WorkManager for background sync operations
- ThreeTenABP for date/time handling

## Performance Considerations

- LazyColumn/LazyRow for efficient list rendering in Compose
- Database queries use Flow for reactive updates
- Background operations use WorkManager with appropriate constraints
- Image loading optimized with Glide
- Memory-conscious PDF generation for receipts
- Proper lifecycle management in Compose screens
