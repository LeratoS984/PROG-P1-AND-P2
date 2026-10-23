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

    /*
     * Message hashing
     * Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        return messageId.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord.toUpperCase()
                + lastWord.toUpperCase();
    }

    /*
     
     * 1 = Send, 2 = Disregard, 3 = Store.
     */
    public String SentMessage(int option) {
        switch (option) {
            case 1:
                SENT_MESSAGES.add(this);
                return "Message successfully sent.";

            case 2:
                return "Press 0 to delete the message.";

            case 3:
                STORED_MESSAGES.add(this);
                return storeMessage();

            default:
                return "Invalid option selected.";
        }
    }

    /*
     * Stores messages in a JSON file.
    
    */
    public String storeMessage() {
        try {
            Files.writeString(
                    STORED_FILE,
                    createJsonArray(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return "Message successfully stored.";
        } catch (IOException exception) {
            return "Message could not be stored.";
        }
    }

    private static String createJsonArray() {
        StringBuilder json = new StringBuilder("[\n");

        for (int index = 0; index < STORED_MESSAGES.size(); index++) {
            Message storedMessage = STORED_MESSAGES.get(index);

            json.append("  {\n");
            json.append("    \"messageId\": \"")
                    .append(storedMessage.messageId).append("\",\n");
            json.append("    \"messageHash\": \"")
                    .append(storedMessage.createMessageHash()).append("\",\n");
            json.append("    \"recipient\": \"")
                    .append(storedMessage.recipient).append("\",\n");
            json.append("    \"message\": \"")
                    .append(escapeJson(storedMessage.messageText)).append("\"\n");
            json.append("  }");

            if (index < STORED_MESSAGES.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");
        return json.toString();
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    /*
     *  returns all messages sent in this session.
     */
    public String printMessages() {
        if (SENT_MESSAGES.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder output = new StringBuilder();

        for (Message sentMessage : SENT_MESSAGES) {
            output.append(sentMessage.formatMessageDetails())
                    .append(System.lineSeparator());
        }

        return output.toString();
    }

    