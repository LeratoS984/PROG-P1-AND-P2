import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a QuickChat message.
 */
public class Message {

    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final List<Message> STORED_MESSAGES = new ArrayList<>();
    private static final Path STORED_FILE = Path.of("stored_messages.json");

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;

    public Message(int messageNumber, String recipient, String messageText) {
        this(createRandomMessageId(), messageNumber, recipient, messageText);
    }
 /*
     * This constructor is used when a known ID is needed for testing.
     */
    public Message(String messageId, int messageNumber,
            String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    private static String createRandomMessageId() {
        long randomNumber = ThreadLocalRandom.current()
                .nextLong(0, 10_000_000_000L);

        return String.format("%010d", randomNumber);
    }

    /*
     * checks that the ID has no more than 10 digits.
     */
    public boolean checkMessageID() {
        return messageId != null && messageId.matches("\\d{1,10}");
    }

    /*
     *  validates an international recipient number.
     */
    public String checkRecipientCell() {
        boolean validRecipient = recipient != null
                && recipient.matches("^\\+\\d{1,3}\\d{9}$");

        if (validRecipient) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }
 /*
     * Checks whether the message is within the 250-character limit.
     */
    public String checkMessageLength() {
        if (messageText == null || messageText.length() == 0) {
            return "Please enter a message of less than 250 characters.";
        }

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int extraCharacters = messageText.length() - 250;

        return "Message exceeds 250 characters by " + extraCharacters
                + "; please reduce the size.";
    }

    