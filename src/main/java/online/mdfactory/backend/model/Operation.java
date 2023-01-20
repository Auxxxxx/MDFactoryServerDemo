package online.mdfactory.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Entity
public class Operation {

    @Id
    private Long id;
    @NotNull
    private String name;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "operation_group_id", referencedColumnName = "id")
    private OperationGroup operationGroup;

    @JsonIgnore
    @ManyToMany(mappedBy = "operations", fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Specification> specifications = new ArrayList<>();

    public Operation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public void setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return id.equals(operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
