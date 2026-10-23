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

    