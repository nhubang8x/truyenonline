package online.hthang.truyenonline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sun.plugin2.message.Message;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Xin lỗi. Liên kết bạn tìm không có hoặc đã bị xóa.";
    public NotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
