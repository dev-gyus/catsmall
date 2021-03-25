package catsmall.cat.mail;

import lombok.Data;

@Data
public class EmailMessage {
    private String to;
    private String message;
    private String subject;
}
