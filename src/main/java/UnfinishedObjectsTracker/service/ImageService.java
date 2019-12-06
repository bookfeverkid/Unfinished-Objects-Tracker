package UnfinishedObjectsTracker.service;

import UnfinishedObjectsTracker.exceptions.FileStorageException;
import UnfinishedObjectsTracker.exceptions.MyFileNotFoundException;
import UnfinishedObjectsTracker.models.Image;
import UnfinishedObjectsTracker.repository.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    public ImageService(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public Image storeFile(MultipartFile image) {
        // Normalize file name
        String imageName = StringUtils.cleanPath(image.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(imageName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + imageName);
            }

           Image thisImage = new Image(imageName, image.getContentType(), image.getBytes());

            return imageDao.save(thisImage);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }

    public Image getFile(String imageId) {
        return imageDao.findById(imageId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + imageId));
    }
}