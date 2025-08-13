# Playwright Kotlin Test Automation Framework

This is a robust test automation framework built with Kotlin and powered by Playwright for modern, reliable, and fast end-to-end testing. It uses Maven for dependency management, JUnit 5 as the test runner, and Koin for lightweight dependency injection.

## ✨ Key Features

- **Modern Tooling**: Utilizes **Playwright** for controlling browsers like Chromium, Firefox, and WebKit.
- **Concise & Safe Language**: Written in **Kotlin**, offering null safety and a clean, modern syntax.
- **Powerful Test Runner**: Leverages **JUnit 5 (Jupiter)** for writing and executing tests.
- **Dependency Injection**: Integrated with **Koin** for managing the lifecycle of core components like `Playwright`, `Browser`, and custom managers.
- **Elegant Reporting**: Generates beautiful, interactive test reports with **Allure Report**.
- **Solid Build Management**: Uses **Apache Maven** for a reliable build process and dependency management.
- **Page Object Model**: Designed with the Page Object Model (POM) pattern for maintainable and scalable test code.
- **Centralized Lifecycle Management**: A custom `ExecutionListener` handles the setup and teardown of the entire test suite, ensuring resources are managed correctly.

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java Development Kit (JDK)**: Version 11 or higher.
- **Apache Maven**: To build the project and manage dependencies.

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd playwright-demo-maven
```

### 2. Install Dependencies

Maven will handle all project dependencies. The first time you build the project, it will download all the necessary libraries defined in `pom.xml`.

Playwright browsers are managed automatically by the framework via the `PlaywrightManager`.

---

## 🧪 Running Tests

You can run the entire test suite using a single Maven command. This command will compile the code, download dependencies, and execute the JUnit 5 tests.

```bash
mvn clean test
```

This will trigger the `ExecutionListener`, which sets up the Koin dependency injection container, initializes Playwright, and runs the tests defined in the `src/test/kotlin` directory.

---

## 📊 Viewing Test Reports

This framework is integrated with Allure to generate detailed and interactive test reports.

### 1. Generate the Report

After running the tests, Allure results will be generated in the `target/allure-results` directory.

### 2. Serve the Report

To view the HTML report, run the following Maven command:

```bash
mvn allure:serve
```

This will open the Allure test report in your default web browser, where you can see a detailed breakdown of test suites, steps, attachments (like screenshots), and execution times.

---

## 🏗️ Project Structure

```
playwright-demo-maven/
├── src
│   ├── main/kotlin/io/github/kinasr/playwright_demo_maven/
│   │   ├── di/              # Koin dependency injection modules
│   │   ├── listener/        # Custom test execution listeners
│   │   ├── pages/           # Page Object classes
│   │   └── playwright_manager/ # Core Playwright lifecycle and browser management
│   └── test/kotlin/io/github/kinasr/playwright_demo_maven/
│       └── tests/           # Test classes
├── pom.xml                  # Maven project configuration
└── README.md                # You are here!
```

---

## 💉 Dependency Injection with Koin

This project uses Koin for managing dependencies in a clean and straightforward way. The core components of the framework are defined in modules within the `src/main/kotlin/io/github/kinasr/playwright_demo_maven/di` package.

- **`mainModule`**: The primary module that aggregates all other modules.
- **`configModule`**: For loading and providing configuration settings.
- **`logModule`**: For setting up and providing loggers.
- **`playwrightModule`**: For managing the `Playwright` and `BrowserManager` instances.
- **`reportModule`**: For handling reporting components, such as Allure test steps.

The `ExecutionListener` is responsible for starting and stopping the Koin container for the entire test run, ensuring that all components are available when needed and properly disposed of afterward.

---

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements or want to fix a bug, please feel free to:

1.  Fork the repository.
2.  Create a new feature branch (`git checkout -b feature/amazing-feature`).
3.  Commit your changes (`git commit -m 'Add some amazing feature'`).
4.  Push to the branch (`git push origin feature/amazing-feature`).
5.  Open a Pull Request.