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