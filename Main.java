package lk.ijse;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static String[][] students = new String[0][0];
    public static String[][] subjects = new String[0][0];

    public static String[][] marks = new String[0][0];

    public static void main(String[] args) {
        welcomePage();
    }

    public static void welcomePage(){
        System.out.println("===========================================================");
        System.out.println("|                  Student Management System              |");
        System.out.println("===========================================================");

        System.out.println("1.Manage Students");
        System.out.println("2.Manage Subject Marks");
        System.out.println("3.Exit");
        System.out.print("Enter your choice [1-3]: ");

        int choice = scanner.nextInt();

        switch (choice){
            case 1:
                clearConsole();
                manageStudentsMenu();
                break;
            case 2:
                clearConsole();
                manageSubjectMarksMenu();
                break;
            case 3:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                welcomePage();
        }
    }

    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            e.printStackTrace();

        }
    }

    private static void manageSubjectMarksMenu() {
        System.out.println("===========================================================");
        System.out.println("|                  Manage Subject Marks                   |");
        System.out.println("===========================================================");

        System.out.println("1. Add new Subject");
        System.out.println("2. Add marks to Students");
        System.out.println("3. Update Subject Marks");
        System.out.println("4. View Subject Marks");
        System.out.println("5. Main Menu");
        System.out.print("Enter your choice [1-5]: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                addNewSubject();
                break;
            case 2:
                clearConsole();
                addMarksToStudents();
                break;
            case 3:
                clearConsole();
                updateSubjectMarks();
                break;
            case 4:
                clearConsole();
                viewAllStudentSubjectMarks();
                break;
            case 5:
                clearConsole();
                welcomePage();
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                manageSubjectMarksMenu();
        }
    }


    private static void viewAllStudentSubjectMarks() {
        System.out.println("===========================================================");
        System.out.println("|                  View Subject Marks                      |");
        System.out.println("===========================================================");

        boolean hasMarks = false;

        for (String[] mark : marks) {
            if (mark != null && mark.length > 2 && !mark[2].isEmpty()) {
                hasMarks = true;
                break;
            }
        }

        if (!hasMarks) {
            System.out.println("No marks have been added for any students.");
            System.out.println("\n\n1. Main Menu");
            System.out.println("2. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    clearConsole();
                    welcomePage();
                    break;
                case 2:
                    System.out.println("Thank you for using Student Management System");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again");
                    viewAllStudentSubjectMarks();
            }
            return;
        }

        System.out.printf("%-15s %-15s %-10s %-15s %-20s %-10s %-10s %-10s %-5s%n",
                "Student ID", "Student Name", "Batch", "Subject ID", "Subject Name", "Marks", "Total", "Avg", "Place");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        double[][] studentMarksStats = new double[students.length][4];

        for (int i = 0; i < students.length; i++) {
            studentMarksStats[i][2] = i;
        }

        for (String[] mark : marks) {
            String studentId = mark[0];
            double markValue = Double.parseDouble(mark[2]);

            for (int i = 0; i < students.length; i++) {
                if (students[i][0].equals(studentId)) {
                    studentMarksStats[i][0] += markValue;
                    studentMarksStats[i][1] += 1;
                    break;
                }
            }
        }

        for (int i = 0; i < studentMarksStats.length - 1; i++) {
            for (int j = i + 1; j < studentMarksStats.length; j++) {
                if (studentMarksStats[i][0] < studentMarksStats[j][0]) {
                    double[] temp = studentMarksStats[i];
                    studentMarksStats[i] = studentMarksStats[j];
                    studentMarksStats[j] = temp;
                }
            }
        }

        int currentPlace = 1;
        double lastTotalMarks = -1;
        for (double[] studentStats : studentMarksStats) {
            if (studentStats[0] != lastTotalMarks) {
                lastTotalMarks = studentStats[0];
                studentStats[3] = currentPlace;
            } else {
                studentStats[3] = currentPlace - 1;
            }
            currentPlace++;
        }

        for (double[] studentStats : studentMarksStats) {
            int studentIndex = (int) studentStats[2];
            String studentId = students[studentIndex][0];
            String studentName = students[studentIndex][1];
            String studentBatch = students[studentIndex][5];
            double totalMarks = studentStats[0];
            double avgMarks = totalMarks / studentStats[1];
            int place = (int) studentStats[3];

            boolean hasPrinted = false;

            for (String[] subject : subjects) {
                String subjectId = subject[0];
                String subjectName = subject[1];
                String marksForStudentSubject = "";

                for (String[] mark : marks) {
                    if (mark[0].equals(studentId) && mark[1].equals(subjectId)) {
                        marksForStudentSubject = mark[2];
                        break;
                    }
                }

                if (marksForStudentSubject.isEmpty()) {
                    continue;
                }

                if (!hasPrinted) {
                    System.out.printf("%-15s %-15s %-10s %-15s %-20s %-10s %-10.2f %-10.2f %-5d%n",
                            studentId, studentName, studentBatch, subjectId, subjectName, marksForStudentSubject, totalMarks, avgMarks, place);
                    hasPrinted = true;
                } else {
                    System.out.printf("%-15s %-15s %-10s %-15s %-20s %-10s %-10.2f %-10.2f %-5d%n",
                            "", "", "", subjectId, subjectName, marksForStudentSubject, totalMarks, avgMarks, place);
                }
            }
        }

        System.out.println("\n\n1. Main Menu");
        System.out.println("2. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                welcomePage();
                break;
            case 2:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                viewAllStudentSubjectMarks();
        }
    }


    private static void updateSubjectMarks() {
        System.out.println("===========================================================");
        System.out.println("|                  Update Subject Marks                   |");
        System.out.println("===========================================================");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.next();

        boolean isFound = false;
        for (String[] student : students) {
            if (student[0].equals(studentId)) {
                isFound = true;
                System.out.print("Student Name: ");
                System.out.println(student[1]);
                System.out.print("\nEnter Subject ID: ");
                String subjectId = scanner.next();

                boolean isSubjectFound = false;
                for (String[] subject : subjects) {
                    if (subject[0].equals(subjectId)) {
                        isSubjectFound = true;
                        String marksForStudentSubject = "";
                        for (String[] mark : marks) {
                            if (mark[0].equals(studentId) && mark[1].equals(subjectId)) {
                                marksForStudentSubject = mark[2];
                                break;
                            }
                        }
                        System.out.print("Subject Name: ");
                        System.out.println(subject[1]);
                        System.out.println("Batch: " + student[5]);
                        System.out.println("Current Marks: " + marksForStudentSubject);

                        System.out.println("Do you want to Update or Delete these marks? [U/D]: ");
                        String choice = scanner.next();

                        if (choice.equalsIgnoreCase("U")) {
                            System.out.print("Enter new Marks: ");
                            double markss = scanner.nextDouble();

                            String[] newMarkDetails = {studentId, subjectId, String.valueOf(markss)};
                            marks = updateMarksArray(newMarkDetails, marks);
                            System.out.println("Marks updated successfully");

                            System.out.println("\n\n1. Update Another Marks");
                            System.out.println("2. Main Menu");
                            System.out.println("3. Exit");

                            int choice1 = scanner.nextInt();

                            switch (choice1) {
                                case 1:
                                    clearConsole();
                                    updateSubjectMarks();
                                    break;
                                case 2:
                                    clearConsole();
                                    welcomePage();
                                    break;
                                case 3:
                                    System.out.println("Thank you for using Student Management System");
                                    System.exit(0);
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again");
                                    updateSubjectMarks();
                            }
                        } else if (choice.equalsIgnoreCase("D")) {
                            marks = deleteMarksFromArray(studentId, subjectId, marks);
                            System.out.println("Marks deleted successfully");

                            System.out.println("\n\n1. Delete Another Marks");
                            System.out.println("2. Main Menu");
                            System.out.println("3. Exit");

                            int choice1 = scanner.nextInt();

                            switch (choice1) {
                                case 1:
                                    clearConsole();
                                    updateSubjectMarks();
                                    break;
                                case 2:
                                    clearConsole();
                                    welcomePage();
                                    break;
                                case 3:
                                    System.out.println("Thank you for using Student Management System");
                                    System.exit(0);
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again");
                                    updateSubjectMarks();
                            }
                        }
                    }
                }

                if (!isSubjectFound) {
                    System.out.println("Subject not found. Please try again");
                    updateSubjectMarks();
                }
            }
        }

        if (!isFound) {
            System.out.println("Student not found. Please try again");
            updateSubjectMarks();
        }
    }

    private static String[][] updateMarksArray(String[] newMarkDetails, String[][] marks) {
        boolean updated = false;
        for (int i = 0; i < marks.length; i++) {
            if (marks[i][0].equals(newMarkDetails[0]) && marks[i][1].equals(newMarkDetails[1])) {
                marks[i][2] = newMarkDetails[2];
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[][] newMarksArray = new String[marks.length + 1][3];
            System.arraycopy(marks, 0, newMarksArray, 0, marks.length);
            newMarksArray[marks.length] = newMarkDetails;
            return newMarksArray;
        }

        return marks;
    }

    private static String[][] deleteMarksFromArray(String studentId, String subjectId, String[][] marks) {
        int count = 0;
        for (String[] mark : marks) {
            if (mark[0].equals(studentId) && mark[1].equals(subjectId)) {
                count++;
            }
        }

        String[][] newMarksArray = new String[marks.length - count][3];
        int index = 0;
        for (String[] mark : marks) {
            if (!(mark[0].equals(studentId) && mark[1].equals(subjectId))) {
                newMarksArray[index++] = mark;
            }
        }

        return newMarksArray;
    }

    private static void addMarksToStudents() {
        System.out.println("===========================================================");
        System.out.println("|                  Add Marks to Students                  |");
        System.out.println("===========================================================");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.next();

        boolean isFound = false;
        for (String[] student : students) {
            if (student[0].equals(studentId)) {
                isFound = true;
                System.out.print("Student Name: ");
                System.out.println(student[1]);
                System.out.print("\nEnter Subject ID: ");
                String subjectId = scanner.next();

                boolean isSubjectFound = false;
                for (String[] subject : subjects) {
                    if (subject[0].equals(subjectId)) {
                        isSubjectFound = true;
                        System.out.print("Subject Name: ");
                        System.out.println(subject[1]);
                        System.out.print("\nEnter Marks: ");
                        double markss = scanner.nextDouble();

                        String[] markDetails = {studentId, subjectId, String.valueOf(markss)};
                        marks = updateMarksArray(markDetails, marks);
                        System.out.println("Marks added successfully");

                        System.out.println("\n\n1. Add Marks to Another Student");
                        System.out.println("2. Main Menu");
                        System.out.println("3. Exit");

                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                clearConsole();
                                addMarksToStudents();
                                break;
                            case 2:
                                clearConsole();
                                welcomePage();
                                break;
                            case 3:
                                System.out.println("Thank you for using Student Management System");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again");
                                addMarksToStudents();
                        }
                    }
                }

                if (!isSubjectFound) {
                    System.out.println("Subject not found. Please try again");
                    addMarksToStudents();
                }
            }
        }

        if (!isFound) {
            System.out.println("Student not found. Please try again");
            addMarksToStudents();
        }
    }

    private static void addNewSubject() {
        System.out.println("===========================================================");
        System.out.println("|                  Add new Subject                        |");
        System.out.println("===========================================================");

        System.out.print("Subject ID: ");
        String subjectId = scanner.next();

        System.out.print("Subject Name: ");
        String subjectName = scanner.next();

        String[] subjectDetails = {subjectId, subjectName};
        subjects = addSubjectDetailsToArray(subjectDetails, subjects);

        System.out.println("\n\n1. Add Another Subject");
        System.out.println("2. Main Menu");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                addNewSubject();
                break;
            case 2:
                clearConsole();
                welcomePage();
                break;
            case 3:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                addNewSubject();
        }
    }

    private static String[][] addSubjectDetailsToArray(String[] subjectDetails, String[][] subjects) {
        String[][] tempArray = new String[subjects.length + 1][2]; // Increase size by 1 for the new subject

        for (int i = 0; i < subjects.length; i++) {
            tempArray[i] = subjects[i];
        }

        tempArray[subjects.length] = subjectDetails;

        return tempArray;
    }



    private static void manageStudentsMenu() {
        System.out.println("===========================================================");
        System.out.println("|                      Manage Students                     |");
        System.out.println("===========================================================");

        System.out.println("1. Add Student");
        System.out.println("2. Update Student");
        System.out.println("3. View Students");
        System.out.println("4. Main Menu");
        System.out.print("Enter your choice [1-4]: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                addStudent();
                break;
            case 2:
                clearConsole();
                updateStudent();
                break;
            case 3:
                clearConsole();
                viewStudents();
                break;
            case 4:
                clearConsole();
                welcomePage();
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                manageStudentsMenu();
        }
    }

    private static void viewStudents() {
        System.out.println("===========================================================");
        System.out.println("|                      View Students                      |");
        System.out.println("===========================================================");

        if (students == null || students.length == 0) {
            System.out.println("No students have been added.");
            System.out.println("\n\n1. Main Menu");
            System.out.println("2. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    clearConsole();
                    welcomePage();
                    break;
                case 2:
                    System.out.println("Thank you for using Student Management System");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again");
                    viewStudents();
            }
            return;
        }

        System.out.printf("%-15s %-15s %-5s %-15s %-20s %-10s%n", "Student ID", "Student Name", "Age", "Contact", "Address", "Batch");
        System.out.println("---------------------------------------------------------------------------------------");

        for (String[] student : students) {
            System.out.printf("%-15s %-15s %-5s %-15s %-20s %-10s%n",
                    student[0], student[1], student[2], student[3], student[4], student[5]);
        }

        System.out.println("\n\n1. Main Menu");
        System.out.println("2. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                welcomePage();
                break;
            case 2:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                viewStudents();
        }
    }

    private static void deleteStudent(String studentId) {
        String[][] tempArray = new String[students.length - 1][6];
        int j = 0;
        for (String[] student : students) {
            if (!student[0].equals(studentId)) {
                tempArray[j] = student;
                j++;
            }
        }

        students = tempArray;

        System.out.println("Student deleted successfully");

        System.out.println("\n\n1. Delete Another Student");
        System.out.println("2. Main Menu");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                updateStudent();
                break;
            case 2:
                clearConsole();
                welcomePage();
                break;
            case 3:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                deleteStudent(studentId);
        }
    }

    private static void updateStudent() {
        System.out.println("===========================================================");
        System.out.println("|                      Update Student                     |");
        System.out.println("===========================================================");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.next();

        searchStudent(studentId);
    }

    private static void searchStudent(String studentId) {
        boolean isFound = false;
        for (String[] student : students) {
            if (student[0].equals(studentId)) {
                isFound = true;
                System.out.println("\n\n");
                System.out.printf("%-15s %-15s %-5s %-15s %-20s %-10s%n", "Student ID", "Student Name", "Age", "Contact", "Address", "Batch");
                System.out.println("---------------------------------------------------------------------------------------");

                System.out.printf("%-15s %-15s %-5s %-15s %-20s %-10s%n",
                        student[0], student[1], student[2], student[3], student[4], student[5]);

                System.out.print("Do you want to update or delete this student? [U/D]: ");
                String choice = scanner.next();

                if (choice.equalsIgnoreCase("U")) {
                    updateFoundStudent(student);
                } else if (choice.equalsIgnoreCase("D")) {
                    deleteStudent(studentId);
                }
            }
        }

        if (!isFound) {
            System.out.println("Student not found. Please try again");
            updateStudent();
        }
    }

    private static void updateFoundStudent(String[] student) {
        System.out.println("===========================================================");
        System.out.println("|                      Update Student                     |");
        System.out.println("===========================================================");

        System.out.print("Student ID: ");
        System.out.println(student[0]);

        System.out.print("Student Name: ");
        String studentName = scanner.next();

        System.out.print("Age: ");
        int age = scanner.nextInt();

        System.out.print("Student Contact: ");
        String studentContact = scanner.next();

        System.out.print("Student Address: ");
        String studentAddress = scanner.next();

        System.out.print("Batch: ");
        String batch = scanner.next();

        String[] studentDetails = {student[0], studentName, String.valueOf(age), studentContact, studentAddress, batch};

        for (int i = 0; i < students.length; i++) {
            if (students[i] == student) {
                students[i] = studentDetails;
            }
        }

        System.out.println("\n\n1. Update Another Student");
        System.out.println("2. Main Menu");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                updateStudent();
                break;
            case 2:
                clearConsole();
                welcomePage();
                break;
            case 3:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                updateFoundStudent(student);
        }
    }

    private static void addStudent() {
        System.out.println("===========================================================");
        System.out.println("|                      Add Student                        |");
        System.out.println("===========================================================");

        System.out.print("Student ID: ");
        String studentId = scanner.next();

        boolean idExists = false;
        for (String[] student : students) {
            if (student[0].equals(studentId)) {
                idExists = true;
                break;
            }
        }

        if (idExists) {
            System.out.println("Student ID already exists. Please enter a unique ID.");
            addStudent();
            return;
        }

        System.out.print("Student Name: ");
        String studentName = scanner.next();

        System.out.print("Age: ");
        int age = scanner.nextInt();

        System.out.print("Student Contact: ");
        String studentContact = scanner.next();

        System.out.print("Student Address: ");
        String studentAddress = scanner.next();

        System.out.print("Batch: ");
        String batch = scanner.next();

        String[] studentDetails = {studentId, studentName, String.valueOf(age), studentContact, studentAddress, batch};
        students = addDetailsToArray(studentDetails, students);

        System.out.println("\n\n1. Add Another Student");
        System.out.println("2. Main Menu");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearConsole();
                addStudent();
                break;
            case 2:
                clearConsole();
                welcomePage();
                break;
            case 3:
                System.out.println("Thank you for using Student Management System");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again");
                addStudent();
        }
    }

    private static String[][] addDetailsToArray(String[] studentDetails, String[][] students) {
        if (students == null) {
            return new String[][] { studentDetails };
        }
        String[][] tempArray = new String[students.length + 1][6];
        for (int i = 0; i < students.length; i++) {
            tempArray[i] = students[i];
        }
        tempArray[students.length] = studentDetails;
        return tempArray;
    }

}