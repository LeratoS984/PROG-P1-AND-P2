import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    @BeforeEach
    public void resetMessages() {
        Message.resetMessagesForTests();
    }

    @Test
    public void testCorrectMessageId() {
        Message message = new Message(
                "1234567890",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertTrue(message.checkMessageID());
    }
    @Test
    public void testValidMessageLength() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageThatIsTooLong() {
        String longMessage = "a".repeat(255);

        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                longMessage
        );

        assertEquals(
                "Message exceeds 250 characters by 5; please reduce the size.",
                message.checkMessageLength()
        );
    }
    @Test
    public void testValidRecipientNumber() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testInvalidRecipientNumber() {
        Message message = new Message(
                "0000000000",
                0,
                "08575975889",
                "Hello Keegan"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testCorrectMessageHash() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testSendMessageOption() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals(1, message.returnTotalMessagess());
    }

    @Test
    public void testDisregardMessageOption() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }

    @Test
    public void testStoreMessageOption() {
        Message message = new Message(
                "0000000000",
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals("Message successfully stored.", message.SentMessage(3));
    }
}

    