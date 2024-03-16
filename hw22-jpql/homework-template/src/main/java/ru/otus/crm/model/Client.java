package ru.otus.crm.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL,targetEntity = Address.class)
    @JoinColumn(name = "address_id")
    @Fetch(FetchMode.JOIN)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        if(address!=null){
            address.setClient(this);
        }
        if(phones!=null){
            phones.forEach(t->t.setClient(this));
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        List<Phone> clonePhones = new ArrayList<>();
        if(phones!=null){
            phones.forEach(t->clonePhones.add(t.clone()));
        }
        return new Client(this.id, this.name,this.address.clone(),clonePhones);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
