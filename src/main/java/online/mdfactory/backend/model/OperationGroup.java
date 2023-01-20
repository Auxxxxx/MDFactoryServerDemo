package online.mdfactory.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Entity
public class OperationGroup {

    @Id
    private long id;
    @NotNull
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "operationGroups", fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Employee> employees = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "operationGroup", fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Operation> operations = new ArrayList<>();

    public OperationGroup() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationGroup)) return false;
        OperationGroup that = (OperationGroup) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
