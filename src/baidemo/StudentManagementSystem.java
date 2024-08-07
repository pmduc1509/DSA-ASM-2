package Baidemo;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Student Management System
 * 
 * This system allows for the management of student records including adding, editing, deleting, sorting, and searching for students.
 * It also handles file operations to save and load student data.
 * 
 * @author phung
 */
public class StudentManagementSystem {

    private static LinkedList<StudentRecord> students = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try {
            loadStudentsFromFile();
        } catch (IOException e) {
            System.out.println("Error loading students from file: " + e.getMessage());
        }

        int choice;
        do {
            System.out.println("\nStudent Management System:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Sort Students by ID");
            System.out.println("5. Sort Students by Marks");
            System.out.println("6. Search Student");
            System.out.println("7. Display All Students");
            System.out.println("8. Exit");

            choice = getValidChoice(1, 8);

            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: editStudent(); break;
                    case 3: deleteStudent(); break;
                    case 4: sortStudentsById(); break;
                    case 5: sortStudentsByMarks(); break;
                    case 6: searchStudent(); break;
                    case 7: displayAllStudents(); break;
                    case 8: 
                        saveStudentsToFile();
                        System.out.println("Exiting the system.");
                        break;
                    default: System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 8);
    }

    private static int getValidChoice(int min, int max) {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            if (input.matches("\\d+")) {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
            }
            System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
        }
    }

    private static void addStudent() {
        try {
            String id, name;
            double marks = -1;

            id = getValidString("Enter Student ID: ", "\\d+", "Invalid ID. Please enter a numeric ID.");
            name = getValidString("Enter Student Name: ", "[^\\d]+", "Invalid name. Please enter a name without numbers.");
            marks = getValidDouble("Enter Student Marks: ", "Invalid marks. Please enter a valid number.");

            students.add(new StudentRecord(id, name, marks));
            System.out.println("Student added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void editStudent() {
        try {
            String id = getValidString("Enter Student ID to edit: ", "\\d+", "Invalid ID. Please enter a numeric ID.");
            for (StudentRecord student : students) {
                if (student.getId().equals(id)) {
                    String name = getValidString("Enter new name: ", "[^\\d]+", "Invalid name. Please enter a name without numbers.");
                    double marks = getValidDouble("Enter new marks: ", "Invalid marks. Please enter a valid number.");

                    student.setName(name);
                    student.setMarks(marks);
                    System.out.println("Student updated successfully.");
                    return;
                }
            }
            System.out.println("Student not found.");
        } catch (Exception e) {
            System.out.println("Error editing student: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            String id = getValidString("Enter Student ID to delete: ", "\\d+", "Invalid ID. Please enter a numeric ID.");
            boolean removed = students.removeIf(student -> student.getId().equals(id));
            System.out.println(removed ? "Student deleted successfully." : "Student not found.");
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    private static void sortStudentsById() {
        try {
            if (students.isEmpty()) {
                System.out.println("No students to sort.");
                return;
            }
            bubbleSortById();
            System.out.println("Students sorted by ID.");
        } catch (Exception e) {
            System.out.println("Error sorting students: " + e.getMessage());
        }
    }

    private static void bubbleSortById() {
        if (students.isEmpty()) return;

        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < students.size() - 1; i++) {
                StudentRecord current = students.get(i);
                StudentRecord next = students.get(i + 1);
                if (current.getId().compareTo(next.getId()) > 0) {
                    students.set(i, next);
                    students.set(i + 1, current);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    private static void sortStudentsByMarks() {
     try {
         if (students.isEmpty()) {
             System.out.println("No students to sort.");
             return;
         }
         selectionSortByMarks();
         System.out.println("Students sorted by Marks.");
     } catch (Exception e) {
         System.out.println("Error sorting students: " + e.getMessage());
     }
 }

    private static void selectionSortByMarks() {
        int n = students.size();
        for (int i = 0; i < n - 1; i++) {
            // Find the index of the minimum element in the unsorted portion
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (students.get(j).getMarks() < students.get(minIndex).getMarks()) {
                    minIndex = j;
                }
            }
            // Swap the found minimum element with the first element of the unsorted portion
            if (minIndex != i) {
                StudentRecord temp = students.get(i);
                students.set(i, students.get(minIndex));
                students.set(minIndex, temp);
            }
        }
    }


    private static void searchStudent() {
        System.out.println("1. Linear Search");
        System.out.println("2. Binary Search (ID only, sorted list required)");
        int searchMethod = getValidChoice(1, 2);

        System.out.print("Enter search criterion (1 for ID, 2 for Name): ");
        String criterion = scanner.nextLine();

        if (criterion.equals("1")) {
            System.out.print("Enter Student ID to search: ");
            String id = scanner.nextLine();
            if (searchMethod == 1) {
                linearSearchById(id);
            } else if (searchMethod == 2) {
                if (isSortedById()) {
                    binarySearchById(id);
                } else {
                    System.out.println("Records are not sorted. Sort them first.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } else if (criterion.equals("2")) {
            System.out.print("Enter Student Name to search: ");
            String name = scanner.nextLine();
            linearSearchByName(name);
        } else {
            System.out.println("Invalid search criterion. Please enter 1 or 2.");
        }
    }

    private static void linearSearchById(String id) {
        for (StudentRecord student : students) {
            if (student.getId().equals(id)) {
                System.out.println(student);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void binarySearchById(String id) {
        int left = 0, right = students.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            StudentRecord student = students.get(mid);
            int cmp = student.getId().compareTo(id);
            if (cmp == 0) {
                System.out.println(student);
                return;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        System.out.println("Student not found.");
    }

    private static void linearSearchByName(String name) {
        boolean found = false;
        for (StudentRecord student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                System.out.println(student);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Student not found.");
        }
    }

    private static boolean isSortedById() {
        for (int i = 1; i < students.size(); i++) {
            if (students.get(i - 1).getId().compareTo(students.get(i).getId()) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void displayAllStudents() {
        try {
            if (students.isEmpty()) {
                System.out.println("No students to display.");
            } else {
                for (StudentRecord student : students) {
                    System.out.println(student);
                }
            }
        } catch (Exception e) {
            System.out.println("Error displaying students: " + e.getMessage());
        }
    }

    private static void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (StudentRecord student : students) {
                writer.write(student.getId() + "," + student.getName() + "," + student.getMarks());
                writer.newLine();
            }
            System.out.println("Students saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving students to file: " + e.getMessage());
        }
    }

    private static void loadStudentsFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String id = parts[0];
                    String name = parts[1];
                    double marks = Double.parseDouble(parts[2]);
                    students.add(new StudentRecord(id, name, marks));
                }
            }
            System.out.println("Students loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading students from file: " + e.getMessage());
            throw e;
        }
    }

    private static String getValidString(String prompt, String regex, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.matches(regex)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    private static double getValidDouble(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                try {
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(errorMessage);
                }
            } else {
                System.out.println(errorMessage);
            }
        }
    }
}
