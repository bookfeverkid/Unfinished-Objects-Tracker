package UnfinishedObjectsTracker.models;


//import UnfinishedObjectsTracker.service.LocalDateTimeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
//import java.sql.Date;
//import java.time.LocalDate;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private int id;

    @Column(name="title")
    @NotEmpty(message="Please provide a title for your project.")
    private String title;

    @Column(name="description")
    @Size(max=20000)
    private String description;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd-MM-yyyy HH-mm")
    @Column(name="creation_date",columnDefinition = "TIMESTAMP")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime creationDate;

    @Column(name="percent_complete")
    @Range(min=0, max=100)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int percentComplete;

    @Transient
    private String date;

    @Transient
    private String imageString;

    @Transient
    private Image image;

    public Project(){

    }

    public Project(int id,
                   @NotEmpty(message = "Please provide a title for your project.") String title,
                   @Size(max = 20000) String description,
                   @Range(min = 0, max = 100) int percentComplete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.percentComplete = percentComplete;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

//    public Set<User> getUsers() {
//        return userId;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public void setUser(Set<User> users) {
//        this.userId = userId;
//    }


    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
