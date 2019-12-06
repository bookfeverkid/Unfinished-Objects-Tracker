package UnfinishedObjectsTracker.models;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private int id;

    private String imageName;

    private String imageType;

    @Lob
    private byte[] data;

    public Image() {

    }

    public Image(String imageName, String imageType, byte[] data) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}


