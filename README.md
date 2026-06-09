# MediaLab: JavaFX Document Management System

## Overview

This repository contains a **desktop-based Document Management System** developed as a final project for the Multimedia Technology course at the National Technical University of Athens (NTUA).

The application is designed to handle document creation, versioning, and user access control. It features a custom data persistence layer using JSON and a Graphical User Interface (GUI) built with JavaFX.

## Key Features

- **Role-Based Access Control (RBAC):** The system supports three distinct user levels (Simple User, Author, and Administrator), each with specific access rights and capabilities.
- **Automated Document Versioning:** When an Author modifies a document's text, the system automatically increments the version number. The application retains the history, allowing Authors and Admins to access up to two previous versions alongside the current one.
- **Real-time Tracking & Notifications:** Users can opt to "track" specific documents. If a tracked document is updated, the user receives a popup notification upon their next successful login.
- **In-Memory State Management with JSON Persistence:** Application data is loaded from JSON files into memory upon initialization for fast execution. State changes are maintained in memory and written back to a dedicated `medialab` directory upon application termination to ensure data persistence.
- **Dynamic JavaFX Dashboard:** The GUI features a split-pane "MediaLab Documents" central window. It includes a live statistics panel (displaying total categories, total documents, and tracked items) alongside a functional workspace for system operations.
- **Search Functionality:** Users can query the database for specific documents using filters such as category, document title, and author name.

## User Roles & Permissions

- **Administrator:** Has full system control. Admins can manage document categories (add, rename, delete) and handle user account creation and deletion. Deleting a category automatically cascades to delete all associated documents and updates user tracking lists accordingly.
- **Author:** Inherits Simple User rights, plus the ability to create, edit, and delete documents within categories they have been granted access to. Documents require a title, author name, category, creation date, and text content.
- **Simple User:** Can view the latest version of documents within their assigned categories. They can read metadata (title, author, creation date, version) and manage their personal tracked documents list.

## Technologies & Architecture

- **Language:** Java
- **GUI Framework:** JavaFX
- **Data Storage:** JSON
- **Design Principles:** Strict adherence to Object-Oriented Programming (OOP) principles.
- **Documentation:** Public methods are documented using standard Javadoc specifications.

### Default Credentials

- Username: `medialab`
- Password: `medialab_2025`
