package movlit.be.common.exception;

public class ImageUploadException extends ResourceNotFoundException {

    public ImageUploadException() {
        super(ErrorMessage.IMAGE_ALREADY_EXISTS_IN_MEMBER);
    }

}
