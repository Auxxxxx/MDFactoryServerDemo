package online.mdfactory.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee implements Serializable {

    @Id
    private String login;
    @NotNull
    private String name;
    @NotNull
    private String password;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "employee_operation_group",
            joinColumns = @JoinColumn(name = "employee_login"),
            inverseJoinColumns = @JoinColumn(name = "operation_group_id")
    )
    private List<OperationGroup> operationGroups = new ArrayList<>();

    public Employee() {
    }

    public void addOperationGroup(OperationGroup operationGroup) {
        operationGroups.add(operationGroup);
    }

    public void removeOperationGroup(OperationGroup operationGroup) {
        operationGroups.remove(operationGroup);
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public void setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return login.equals(employee.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
