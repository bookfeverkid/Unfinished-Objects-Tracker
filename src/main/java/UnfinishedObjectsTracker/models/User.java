package UnfinishedObjectsTracker.models;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    @Column(name = "email",unique = true)
    @NotEmpty(message = "*Please provide an email")
    @Email(message="Please provide a valid email")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    private String email;

    @Column(name="username", unique = true)
    @Size(min=8, max=15, message = "Username length must be 8 to 15 characters.")
    @Pattern(regexp="[a-zA-Z]*", message = "Username can only contain alphabetic characters.")
    @NotEmpty(message= "Please provide a username")
    private String username;

    @Column(name = "password")
    @Size(min=6, message = "Password should have at least 6 characters.")
    @NotEmpty(message = "*Please provide your password")
    private String password;

//    @Column(name = "name")
//    @NotEmpty(message = "*Please provide your name")
//    private String name;

//    @Column(name = "last_name")
//    @NotEmpty(message = "*Please provide your last name")
//    private String lastName;

    @Column(name = "active")
    private int active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User(int id, @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email") String email, @Length(min = 5, message = "*Your password must have at least 5 characters") @NotEmpty(message = "*Please provide your password") String password, @NotEmpty(message="Please provide a username") String username, int active, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.active = active;
        this.roles = roles;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}