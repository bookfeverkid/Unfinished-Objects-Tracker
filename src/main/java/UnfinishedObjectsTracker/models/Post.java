package UnfinishedObjectsTracker.models;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private int id;

    @Column(name="title")
    @NotEmpty(message="Please provide a title for your project.")
    private String title;

    @Column(name="description")
    @Size(max=20000)
    private String description;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd-MM-yyyy HH-mm")
    @Column(name="post_date",columnDefinition = "TIMESTAMP")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime postDate;

    @Transient
    private String pDate;

    @Column(name="project_id")
    private int projectId;

    public Post(){
    }

    public Post(int id,
                @NotEmpty(message = "Please provide a title for your project.") String title,
                @Size(max = 20000) String description,
                int projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectId = projectId;
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

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
