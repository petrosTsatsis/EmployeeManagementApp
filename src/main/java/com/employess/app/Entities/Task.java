package com.employess.app.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_executor")
    private String taskExecutor;

    @Column(name = "deadline")
    private String deadline;

    @Column(name = "completion_date")
    private String completionDate;

    @Column(name = "bonus")
    private String bonus;

    @ManyToMany(mappedBy = "tasks")
    @JsonIgnore
    List<Employee> employees;

    @ManyToMany(mappedBy = "tasks")
    @JsonIgnore
    List<Department> departments;

    public Task(String taskTitle, String taskExecutor, String deadline, String completionDate, String bonus) {
        this.taskTitle = taskTitle;
        this.taskExecutor = taskExecutor;
        this.deadline = deadline;
        this.completionDate = completionDate;
        this.bonus = bonus;
    }

    public Task(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(String taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
