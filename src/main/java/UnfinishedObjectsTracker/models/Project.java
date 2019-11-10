package UnfinishedObjectsTracker.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private int id;

    @Column(name="title")
    @NotEmpty(message="Please provide a title for your project.")
    @Size(min=3, max=30)
    private String title;

    @Column(name="description")
    @Size(max=20000)
    private String description;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ownership", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<User> userId;

    public void addItem(User i){userId.add(i);}

    public Project(){

    }

    public Project(int id, @NotEmpty(message = "Please provide a title for your project.")
    @Size(min = 3, max = 30) String title, @Size(max = 20000) String description, Set<User> users) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return userId;
    }

    public void setUser(Set<User> users) {
        this.userId = userId;
    }
}
