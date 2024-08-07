/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baidemo;

/**
 *
 * @author ADMIN
 */
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentManagementSystemWithArray {

    private static Student[] students = new Student[100]; // Initial capacity of 100
    private static int studentCount = 0; // Number of students currently in the array
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        loadStudentsFromFile();

        int choice;

        do {
            System.out.println("\nStudent Management System:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Sort Students");
            System.out.println("5. Search Student");
            System.out.println("6. Display All Students");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        editStudent();
                        break;
                    case 3:
                        deleteStudent();
                        break;
                    case 4:
                        sortStudents();
                        break;
                    case 5:
                        searchStudent();
                        break;
                    case 6:
                        displayAllStudents();
                        break;
                    case 7:
                        saveStudentsToFile();
                        System.out.println("Exiting the system.");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 7.");
                scanner.nextLine(); // Consume invalid input
                choice = -1; // Keep loop running
            }
        } while (choice != 7);
    }

    private static void addStudent() {
        if (studentCount >= students.length) {
            System.out.println("Student array is full. Cannot add more students.");
            return;
        }

        String id, name;
        double marks;

        System.out.print("Enter Student ID: ");
        id = scanner.nextLine();
        if (id.trim().isEmpty()) {
            System.out.println("Student ID cannot be empty!");
            return;
        }

        System.out.print("Enter Student Name: ");
        name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Student Name cannot be empty!");
            return;
        } else if (name.matches(".*\\d.*")) {
            System.out.println("Student Name cannot contain numbers!");
            return;
        }

        System.out.print("Enter Student Marks: ");
        try {
            marks = scanner.nextDouble();
            if (marks < 0 || marks > 10) {
                System.out.println("Marks must be between 0 and 10.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Marks must be a number.");
            scanner.nextLine(); // Consume invalid input
            return;
        }

        students[studentCount++] = new Student(id, name, marks);
        System.out.println("Student added successfully.");
    }

    private static void editStudent() {
        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Student Name cannot be empty!");
                    return;
                } else if (name.matches(".*\\d.*")) {
                    System.out.println("Student Name cannot contain numbers!");
                    return;
                }

                System.out.print("Enter new marks: ");
                try {
                    double marks = scanner.nextDouble();
                    if (marks < 0 || marks > 10) {
                        System.out.println("Marks must be between 0 and 10.");
                        return;
                    }
                    students[i].setName(name);
                    students[i].setMarks(marks);
                    System.out.println("Student updated successfully.");
                    return;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Marks must be a number.");
                    scanner.nextLine(); // Consume invalid input
                    return;
                }
            }
        }
        System.out.println("Student not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                // Shift students to fill the gap
                for (int j = i; j < studentCount - 1; j++) {
                    students[j] = students[j + 1];
                }
                students[--studentCount] = null; // Remove last element
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void sortStudents() {
        if (studentCount == 0) {
            System.out.println("No students to sort.");
            return;
        }

        System.out.println("Choose sorting method:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.print("Enter your choice: ");
        int sortChoice;
        try {
            sortChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (sortChoice) {
                case 1:
                    bubbleSortById();
                    System.out.println("Students sorted by ID using Bubble Sort.");
                    break;
                case 2:
                    selectionSortById();
                    System.out.println("Students sorted by ID using Selection Sort.");
                    break;
                default:
                    System.out.println("Invalid choice! Defaulting to Bubble Sort.");
                    bubbleSortById();
                    System.out.println("Students sorted by ID using Bubble Sort.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    private static void bubbleSortById() {
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < studentCount - 1; i++) {
                if (students[i].getId().compareTo(students[i + 1].getId()) > 0) {
                    // Swap students
                    Student temp = students[i];
                    students[i] = students[i + 1];
                    students[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
    }

    private static void selectionSortById() {
        for (int i = 0; i < studentCount - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < studentCount; j++) {
                if (students[j].getId().compareTo(students[minIndex].getId()) < 0) {
                    minIndex = j;
                }
            }
            Student temp = students[minIndex];
            students[minIndex] = students[i];
            students[i] = temp;
        }
    }

        private static void searchStudent() {
        System.out.println("1. Linear Search");
        System.out.println("2. Binary Search");
        System.out.print("Choose search method: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine();

        if (choice == 1) {
            linearSearch(id);
        } else if (choice == 2) {
            if (isSorted()) {
                binarySearch(id);
            } else {
                System.out.println("Records are not sorted. Sort them first.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void linearSearch(String id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                System.out.println(students[i]);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void binarySearch(String id) {
        int left = 0, right = studentCount - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = students[mid].getId().compareTo(id);
            if (cmp == 0) {
                System.out.println(students[mid]);
                return;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        System.out.println("Student not found.");
    }

private static boolean isSorted() {
    for (int i = 1; i < studentCount; i++) {
        if (students[i].getId().compareTo(students[i - 1].getId()) < 0) {
            return false;
        }
    }
    return true;
}


    private static void displayAllStudents() {
        if (studentCount == 0) {
            System.out.println("No students to display.");
        } else {
            for (int i = 0; i < studentCount; i++) {
                System.out.println(students[i]);
            }
        }
    }

    private static void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < studentCount; i++) {
                writer.write(students[i].getId() + "," + students[i].getName() + "," + students[i].getMarks());
                writer.newLine();
            }
            System.out.println("Students saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving students to file: " + e.getMessage());
        }
    }

    private static void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String id = parts[0];
                    String name = parts[1];
                    double marks = Double.parseDouble(parts[2]);
                    if (studentCount >= students.length) {
                        // Expand the array if necessary
                        expandStudentArray();
                    }
                    students[studentCount++] = new Student(id, name, marks);
                }
            }
            System.out.println("Students loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading students from file: " + e.getMessage());
        }
    }

    private static void expandStudentArray() {
        Student[] newArray = new Student[students.length * 2];
        System.arraycopy(students, 0, newArray, 0, students.length);
        students = newArray;
    }

    static class Student {
        private String id;
        private String name;
        private double marks;

        public Student(String id, String name, double marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getMarks() {
            return marks;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMarks(double marks) {
            this.marks = marks;
        }

        public String getRank() {
            if (marks >= 0 && marks < 5.0) {
                return "Fail";
            } else if (marks >= 5.0 && marks < 6.5) {
                return "Medium";
            } else if (marks >= 6.5 && marks < 7.5) {
                return "Good";
            } else if (marks >= 7.5 && marks < 9.0) {
                return "Very Good";
            } else if (marks >= 9.0 && marks <= 10.0) {
                return "Excellent";
            }
            return "Invalid Marks";
        }

        @Override
        public String toString() {
            return "Student ID: " + id + ", Name: " + name + ", Marks: " + marks + ", Rank: " + getRank();
        }
    }
}

