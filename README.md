# Task Manager Application

## Overview

The **Task Manager Application** is a desktop application designed to help users manage their personal tasks efficiently. It provides features such as task creation, modification, deletion, and categorization based on priority, status, and deadlines. The application is built using **Java** and **JavaFX** for the user interface, with **PostgreSQL** as the backend database. It also includes features like user authentication, task filtering, and notifications.

This project was developed as part of an academic project at **Ecole Marocaine des Sciences de l'Ingénieur (EMSI)** under the guidance of **Mme Bouchra HONNIT**.

---
![Screenshot from 2025-01-25 10-03-42](https://github.com/user-attachments/assets/53aaf242-034b-4d9c-af6c-e3288fc203b0)

## Features

### Core Features
- **User Authentication**: Users can sign up, log in, and log out. A token-based authentication system ensures secure access.
- **Task Management**:
  - Add new tasks with details like title, description, start/end dates, priority, and status.
  - Modify existing tasks.
  - Delete tasks with confirmation.
- **Task Organization**:
  - Filter tasks by priority, status, or category.
  - View tasks in a structured dashboard.
- **Notifications**: Users receive reminders for upcoming tasks.

### Technical Features
- **Database**: PostgreSQL is used for storing user data, tasks, and boards.
- **UML Design**: The application follows a well-structured UML design, including use case diagrams and class diagrams.
- **Unit Testing**: Extensive unit tests ensure the reliability of the application.

---

## Technologies Used

- **Programming Language**: Java
- **Framework**: JavaFX (for GUI)
- **Database**: PostgreSQL
- **UML Tool**: StarUML
- **IDE**: IntelliJ IDEA
- **Version Control**: Git

---

## Project Structure

The project is organized into the following modules:

```
src/
├── auth/                # Authentication module (e.g., AuthManager)
├── controllers/         # Controllers for handling UI logic
├── models/              # Data models (e.g., Task, User, Board)
├── utils/               # Utility classes (e.g., Database, DatabaseConnection)
├── resources/           # Resources like icons, images, and styles
│   ├── fonts/           # Font files
│   ├── icons/           # Icons for the UI
│   ├── images/          # Application images
│   └── styles/          # CSS styles for the UI
└── views/               # FXML files for the UI
```

---

## Installation

### Prerequisites
- **Java Development Kit (JDK)**: Version 11 or higher.
- **PostgreSQL**: Install and set up a PostgreSQL database.
- **JavaFX**: Ensure JavaFX is configured in your development environment.

### Steps to Run the Application
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/task-manager.git
   cd task-manager
   ```

2. **Set Up the Database**:
   - Create a PostgreSQL database named `task_manager`.
   - Update the `DatabaseConnection.java` file with your database credentials.

3. **Run the Application**:
   - Open the project in IntelliJ IDEA or any Java IDE.
   - Ensure JavaFX is properly configured.
   - Run the `Main.java` file to start the application.

---

## Screenshots

### Dashboard
![Screenshot from 2025-01-25 10-03-42](https://github.com/user-attachments/assets/53aaf242-034b-4d9c-af6c-e3288fc203b0)

### Login Page
![Screenshot from 2025-01-23 11-18-30](https://github.com/user-attachments/assets/27831dec-b12c-47c6-8f5a-80278866c68c)

### Sign up
![Screenshot from 2025-01-23 11-18-50](https://github.com/user-attachments/assets/5d5e24c8-94bd-4a93-a873-c223a4b92138)

### Add Task Page
![Screenshot from 2025-01-23 11-20-46](https://github.com/user-attachments/assets/a8cbd86c-2b3d-4e3f-930b-3f6af5ae1620)

### Edit Task Page
![Screenshot from 2025-01-23 11-20-46](https://github.com/user-attachments/assets/38ee0388-1e99-4b6c-9296-92dee77b3fb4)

---

## Testing

The application includes unit tests for all major components. To run the tests:

1. Navigate to the `TestModels` and `TestDatabase` directories.
2. Run the test files (e.g., `TestTask.java`, `TestDatabase.java`) using your IDE's testing framework.

---

## Future Improvements

- **Cloud Synchronization**: Allow users to sync tasks across multiple devices using cloud services.
- **Multi-User Support**: Add roles and permissions for team-based task management.
- **Interactive Calendar**: Integrate a calendar view for better task visualization.
- **Enhanced Security**: Implement two-factor authentication for added security.
- **Performance Optimization**: Improve the application's performance for handling large datasets.

---

## Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and push to the branch.
4. Submit a pull request with a detailed description of your changes.

---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- **Mme Bouchra HONNIT** for her guidance and support throughout the project.
- **EMSI** for providing the resources and environment to develop this application.
- **JavaFX** and **PostgreSQL** communities for their excellent documentation and tools.

---

## Contact

For any questions or feedback, feel free to reach out:

- **Name**: Moustaid Youssef
- **Email**: moustaidbusiness@gmail.com

---

Thank you for checking out the Task Manager Application! We hope it helps you stay organized and productive. 🚀
