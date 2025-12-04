import java.util.*;

/**
 * Student Record Management System (Console)
 * Single-file Java implementation using a singly linked list.
 *
 * Features:
 * - Add student (name, roll, branch, marks)
 * - Search student by roll number
 * - Update student information
 * - Delete student by roll number
 * - Display all records (original insertion order)
 * - Display records sorted by roll or marks
 *
 * Compile: javac StudentManagement.java
 * Run:     java StudentManagement
 */

public class StudentManagement {
    // Node containing a Student
    private static class Node {
        Student data;
        Node next;
        Node(Student s) { data = s; next = null; }
    }

    // Student data class
    private static class Student {
        int roll;
        String name;
        String branch;
        double marks;

        Student(int roll, String name, String branch, double marks) {
            this.roll = roll;
            this.name = name;
            this.branch = branch;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return String.format("Roll: %d | Name: %s | Branch: %s | Marks: %.2f",
                                 roll, name, branch, marks);
        }
    }

    // Singly linked list for Students
    private Node head = null;

    // Insert at end (maintains insertion order)
    public void insert(Student s) {
        Node newNode = new Node(s);
        if (head == null) {
            head = newNode;
            return;
        }
        Node cur = head;
        while (cur.next != null) cur = cur.next;
        cur.next = newNode;
    }

    // Search by roll number, return Student or null
    public Student search(int roll) {
        Node cur = head;
        while (cur != null) {
            if (cur.data.roll == roll) return cur.data;
            cur = cur.next;
        }
        return null;
    }

    // Update student fields by roll number; returns true if updated
    public boolean update(int roll, String newName, String newBranch, Double newMarks) {
        Node cur = head;
        while (cur != null) {
            if (cur.data.roll == roll) {
                if (newName != null && !newName.isEmpty()) cur.data.name = newName;
                if (newBranch != null && !newBranch.isEmpty()) cur.data.branch = newBranch;
                if (newMarks != null) cur.data.marks = newMarks;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    // Delete student by roll number; returns true if deleted
    public boolean delete(int roll) {
        if (head == null) return false;
        if (head.data.roll == roll) {
            head = head.next;
            return true;
        }
        Node prev = head;
        Node cur = head.next;
        while (cur != null) {
            if (cur.data.roll == roll) {
                prev.next = cur.next;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    // Get list of students (in insertion order)
    public List<Student> toList() {
        List<Student> list = new ArrayList<>();
        Node cur = head;
        while (cur != null) {
            list.add(cur.data);
            cur = cur.next;
        }
        return list;
    }

    // Display all records in insertion order
    public void displayAll() {
        List<Student> list = toList();
        if (list.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }
        System.out.println("Student Records (in insertion order):");
        for (Student s : list) System.out.println("  " + s);
    }

    // Display records sorted either by "roll" or "marks"
    public void displaySorted(String sortBy) {
        List<Student> list = toList();
        if (list.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }
        if ("roll".equalsIgnoreCase(sortBy)) {
            list.sort(Comparator.comparingInt(a -> a.roll));
            System.out.println("Student Records (sorted by roll):");
        } else if ("marks".equalsIgnoreCase(sortBy)) {
            list.sort((a, b) -> Double.compare(b.marks, a.marks)); // descending by marks
            System.out.println("Student Records (sorted by marks, high->low):");
        } else {
            System.out.println("Unknown sort option. Use 'roll' or 'marks'.");
            return;
        }
        for (Student s : list) System.out.println("  " + s);
    }

    // --- Main interactive console ---
    public static void main(String[] args) {
        StudentManagement sm = new StudentManagement();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Student Record Management System ===");
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add student");
            System.out.println("2. Search student by roll");
            System.out.println("3. Update student by roll");
            System.out.println("4. Delete student by roll");
            System.out.println("5. Display all records");
            System.out.println("6. Display records sorted");
            System.out.println("7. Exit");
            System.out.print("Enter choice (1-7): ");

            String input = sc.nextLine().trim();
            switch (input) {
                case "1": // Add
                    try {
                        System.out.print("Enter roll (integer): ");
                        int roll = Integer.parseInt(sc.nextLine().trim());
                        if (sm.search(roll) != null) {
                            System.out.println("Error: a student with this roll already exists.");
                            break;
                        }
                        System.out.print("Enter name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Enter branch: ");
                        String branch = sc.nextLine().trim();
                        System.out.print("Enter marks (0-100): ");
                        double marks = Double.parseDouble(sc.nextLine().trim());
                        sm.insert(new Student(roll, name, branch, marks));
                        System.out.println("Student added successfully.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Roll and marks must be numeric.");
                    }
                    break;

                case "2": // Search
                    try {
                        System.out.print("Enter roll to search: ");
                        int r = Integer.parseInt(sc.nextLine().trim());
                        Student found = sm.search(r);
                        if (found != null) System.out.println("Found: " + found);
                        else System.out.println("Student with roll " + r + " not found.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid roll number.");
                    }
                    break;

                case "3": // Update
                    try {
                        System.out.print("Enter roll to update: ");
                        int r = Integer.parseInt(sc.nextLine().trim());
                        Student existing = sm.search(r);
                        if (existing == null) {
                            System.out.println("Student not found.");
                            break;
                        }
                        System.out.println("Leave a field blank to keep it unchanged.");
                        System.out.print("New name (current: " + existing.name + "): ");
                        String newName = sc.nextLine().trim();
                        System.out.print("New branch (current: " + existing.branch + "): ");
                        String newBranch = sc.nextLine().trim();
                        System.out.print("New marks (current: " + existing.marks + "): ");
                        String marksStr = sc.nextLine().trim();
                        Double newMarks = null;
                        if (!marksStr.isEmpty()) newMarks = Double.parseDouble(marksStr);
                        boolean updated = sm.update(r, newName.isEmpty() ? null : newName,
                                                       newBranch.isEmpty() ? null : newBranch,
                                                       newMarks);
                        System.out.println(updated ? "Student updated successfully." : "Update failed.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid marks/roll input.");
                    }
                    break;

                case "4": // Delete
                    try {
                        System.out.print("Enter roll to delete: ");
                        int rdel = Integer.parseInt(sc.nextLine().trim());
                        boolean deleted = sm.delete(rdel);
                        System.out.println(deleted ? "Student deleted successfully." : "Student not found.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid roll number.");
                    }
                    break;

                case "5": // Display all
                    sm.displayAll();
                    break;

                case "6": // Display sorted
                    System.out.print("Sort by ('roll' or 'marks'): ");
                    String sortBy = sc.nextLine().trim();
                    sm.displaySorted(sortBy);
                    break;

                case "7": // Exit
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Enter number between 1 and 7.");
            }
        }
        System.out.println("Exiting. Goodbye!");
        sc.close();
    }
}
