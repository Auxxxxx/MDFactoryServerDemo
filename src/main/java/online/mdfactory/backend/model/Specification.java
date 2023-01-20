package online.mdfactory.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.*;

@Entity
public class Specification {

    @Id
    private long id;
    @NotNull
    private String name;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "specification_operation",
            joinColumns = @JoinColumn(name = "specification_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_id")
    )
    private List<Operation> operations = new ArrayList<>();

    public Specification() {
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
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

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specification)) return false;
        Specification that = (Specification) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
