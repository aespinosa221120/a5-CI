# Assignment 5 – SE333 CI Pipeline

![Build Status](https://github.com/aespinosa221120/Assignment5_code/actions/workflows/SE333_CI.yml/badge.svg)

## Project Overview

This project demonstrates automated testing and code quality analysis using **GitHub Actions**, **Maven**, and **Java**.  
The workflow automatically runs on every push to `main` and feature branches, performing:

- **Static Analysis:** Checkstyle checks code style and best practices.
- **Unit & Integration Testing:** JUnit 5, Mockito, and AssertJ ensure correctness.
- **Code Coverage:** JaCoCo generates coverage reports after tests.

## CI Workflow

The GitHub Actions workflow `SE333_CI.yml` performs the following steps:

1. Checks out the repository.
2. Sets up Java (Temurin 23).
3. Caches Maven dependencies.
4. Runs Checkstyle (does not fail on violations).
5. Runs tests.
6. Uploads Checkstyle and JaCoCo reports as artifacts.

## Artifacts

The workflow produces the following artifacts for review:

- `checkstyle-report` – XML report for code style violations.
- `jacoco-report` – XML coverage report from JaCoCo.

## Status

- ✅ GitHub Actions executed successfully.
- ✅ Static analysis, tests, and coverage all completed.
- ✅ Artifacts are available for download from workflow runs.