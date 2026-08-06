package org.example.view;

import org.example.controller.LibraryController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LibraryView — MVC-এর "V"। এই ক্লাসে শুধু Swing UI কম্পোনেন্ট আর
 * তাদের event listener আছে। কোনো business rule (double-issue block,
 * validation ইত্যাদি) এখানে লেখা নেই — সব সিদ্ধান্ত LibraryController-কে
 * জিজ্ঞেস করে নেওয়া হয়। এই আলাদাকরণটাই MVC প্যাটার্নের মূল কথা।
 */
public class LibraryView {

    private final LibraryController controller;

    private JFrame frame;
    private JTextArea outputArea;

    private JTextField studentIdField, studentNameField;
    private JTextField bookIdField, bookTitleField, bookAuthorField;
    private JTextField issueBookIdField, issueStudentIdField;

    public LibraryView(LibraryController controller) {
        this.controller = controller;
        initializeGUI();
        refreshOutput();
    }

    private void initializeGUI() {
        frame = new JFrame("University Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("University Library Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JPanel studentPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Registration & Delete"));
        studentPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        studentPanel.add(studentIdField);
        studentPanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        studentPanel.add(studentNameField);

        JButton regStudentBtn = new JButton("Register Student");
        JButton delStudentBtn = new JButton("Delete Student");
        studentPanel.add(regStudentBtn);
        studentPanel.add(delStudentBtn);

        JPanel bookPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        bookPanel.setBorder(BorderFactory.createTitledBorder("Add New Book"));
        bookPanel.add(new JLabel("Book ID (ISBN):"));
        bookIdField = new JTextField();
        bookPanel.add(bookIdField);
        bookPanel.add(new JLabel("Book Title:"));
        bookTitleField = new JTextField();
        bookPanel.add(bookTitleField);
        bookPanel.add(new JLabel("Author Name:"));
        bookAuthorField = new JTextField();
        bookPanel.add(bookAuthorField);

        JButton addBookBtn = new JButton("Add Book");
        bookPanel.add(addBookBtn);

        JPanel transactionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Issue / Return Book"));
        transactionPanel.add(new JLabel("Book ID:"));
        issueBookIdField = new JTextField();
        transactionPanel.add(issueBookIdField);
        transactionPanel.add(new JLabel("Student ID:"));
        issueStudentIdField = new JTextField();
        transactionPanel.add(issueStudentIdField);

        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        transactionPanel.add(issueBtn);
        transactionPanel.add(returnBtn);

        inputPanel.add(studentPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(bookPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(transactionPanel);

        frame.add(inputPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Library Database Status (Live Output)"));
        frame.add(scrollPane, BorderLayout.CENTER);

        regStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = studentIdField.getText().trim();
                String name = studentNameField.getText().trim();
                boolean success = controller.registerMember(id, name);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Student Registered Successfully!");
                    clearStudentFields();
                    refreshOutput();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill both Student ID and Name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        delStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = studentIdField.getText().trim();
                boolean success = controller.deleteMember(id);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Student Deleted Successfully!");
                    clearStudentFields();
                    refreshOutput();
                } else {
                    JOptionPane.showMessageDialog(frame, "Student ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = bookIdField.getText().trim();
                String title = bookTitleField.getText().trim();
                String author = bookAuthorField.getText().trim();
                boolean success = controller.addBook(isbn, title, author);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Book Added Successfully!");
                    clearBookFields();
                    refreshOutput();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill all book fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        issueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = issueBookIdField.getText().trim();
                String sId = issueStudentIdField.getText().trim();
                if (controller.issueBook(isbn, sId)) {
                    JOptionPane.showMessageDialog(frame, "Book Successfully Issued!");
                    clearTransactionFields();
                    refreshOutput();
                } else {
                    JOptionPane.showMessageDialog(frame, "Issue Failed! Check Book availability or IDs.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = issueBookIdField.getText().trim();
                String sId = issueStudentIdField.getText().trim();
                if (controller.returnBook(isbn, sId)) {
                    JOptionPane.showMessageDialog(frame, "Book Successfully Returned!");
                    clearTransactionFields();
                    refreshOutput();
                } else {
                    JOptionPane.showMessageDialog(frame, "Return Failed! Invalid Book ID or Student ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void refreshOutput() {
        outputArea.setText(controller.getStatusReport());
    }

    private void clearStudentFields() {
        studentIdField.setText("");
        studentNameField.setText("");
    }

    private void clearBookFields() {
        bookIdField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
    }

    private void clearTransactionFields() {
        issueBookIdField.setText("");
        issueStudentIdField.setText("");
    }
}
