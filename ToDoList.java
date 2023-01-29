import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class ToDoList extends JFrame {
    // JList to display tasks
    static JList<Task> taskList;
    // JButton to add a task
    static JButton addTaskButton;
    // JButton to remove a task
    static JButton removeTaskButton;
    // JButton to mark task as completed
    static JButton doneTaskButton;
    // JTextField to input new task name
    static JTextField newTaskField;
    // JTextField to input new task deadline
    static JTextField deadlineField;
    // DefaultListModel to store tasks in the JList
    static DefaultListModel<Task> taskModel;

    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        // Create the JList and its model
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        taskList.setLayoutOrientation(JList.VERTICAL);
        taskList.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(taskList);
        listScroller.setPreferredSize(new Dimension(250, 80));
        frame.add(listScroller, BorderLayout.CENTER);

        // Create the "Add Task" button and text field
        addTaskButton = new JButton("Add Task");
        newTaskField = new JTextField(20);
        deadlineField = new JTextField(20);
        addTaskButton.addActionListener(new AddTaskListener());
        JPanel addTaskPanel = new JPanel();
        addTaskPanel.add(new JLabel("Task:"));
        addTaskPanel.add(newTaskField);
        addTaskPanel.add(new JLabel("Deadline:"));
        addTaskPanel.add(deadlineField);
        addTaskPanel.add(addTaskButton);
        addTaskPanel.setPreferredSize(new Dimension(150, 80));
        frame.add(addTaskPanel, BorderLayout.SOUTH);

        // Create the "Remove Task" button
        removeTaskButton = new JButton("Remove");
        removeTaskButton.addActionListener(new RemoveTaskListener());
        JPanel removeTaskPanel = new JPanel();
        removeTaskPanel.add(removeTaskButton);
        frame.add(removeTaskPanel, BorderLayout.EAST);

        // Create the "Done Task" button
        doneTaskButton = new JButton("   Done   ");
        doneTaskButton.addActionListener(new DoneTaskListener());
        JPanel doneTaskPanel = new JPanel();
        doneTaskPanel.add(doneTaskButton);
        frame.add(doneTaskPanel, BorderLayout.EAST);


        //Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(removeTaskButton);
        buttonPanel.add(doneTaskButton);
        frame.add(buttonPanel, BorderLayout.EAST);

        // Show the JFrame
        frame.pack();
        frame.setVisible(true);

        // Number all tasks
        numberTasks();
    }

    static class AddTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String newTask = newTaskField.getText();
            String deadline = deadlineField.getText();
            if (!newTask.isEmpty() && !deadline.isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date();
                Task task = new Task(newTask, deadline, formatter.format(date));
                taskModel.addElement(task);
                newTaskField.setText("");
                deadlineField.setText("");
                // Update task numbering
                numberTasks();
            }
        }
    }

    static class RemoveTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskModel.remove(selectedIndex);
                // Update task numbering
                numberTasks();
            }
        }
    }

    static class DoneTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task selectedTask = taskModel.get(selectedIndex);
                if (!selectedTask.isCompleted()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = new Date();
                    selectedTask.setCompleted(true);
                    selectedTask.setCompletedTime(formatter.format(date));
                    taskList.updateUI();
                }
            }
        }
    }

    // Method to number all tasks
    public static void numberTasks() {
        for (int i = 0; i < taskModel.size(); i++) {
            Task task = taskModel.get(i);
            task.setNumber(i + 1);
        }
        taskList.updateUI();
    }
}

class Task {
    private String name;
    private String deadline;
    private String date;
    private boolean completed;
    private String completedTime;
    private int number;

    public Task(String name, String deadline, String date) {
        this.name = name;
        this.deadline = deadline;
        this.date = date;
        this.completed = false;
        this.completedTime = "";
        this.number = 0;
    }

    public String toString() {
        if (!completed) {
            return number + ". " + name + " || (Deadline: " + deadline + ") || (Added on: " + date + ")";
        } else {
            return number + ". " + name + " || (Completed on: " + completedTime + ") || (Added on: " + date + ")";
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}


