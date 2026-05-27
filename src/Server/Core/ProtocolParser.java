package Server.Core;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import Rules.Constants;

/**
 * ProtocolParser - Parses DataBridge protocol messages from ByteBuffer
 * 
 * Protocol format:
 * - Messages are text-based, delimited by newline (\n)
 * - Command parts are separated by Constants.DELIMITER (ASCII 29)
 * - Format: TYPE <delim> COMMAND <delim> DATA...
 * - Special case: KEY exchange uses space as separator
 * 
 * This class handles:
 * - Reading complete messages from ByteBuffer
 * - Handling partial messages (buffering incomplete data)
 * - Message validation and parsing
 */
public class ProtocolParser {
    private static final byte NEWLINE = '\n';
    private static final byte DELIMITER = (byte) Constants.DELIMITER.charAt(0);
    private static final int MAX_MESSAGE_SIZE = 64 * 1024; // 64KB max message
    
    private final ByteBuffer readBuffer;
    private final StringBuilder messageBuffer;
    
    public ProtocolParser(int bufferSize) {
        this.readBuffer = ByteBuffer.allocate(bufferSize);
        this.messageBuffer = new StringBuilder();
    }
    
    /**
     * Try to extract a complete message from the read buffer
     * Returns null if no complete message is available yet
     * @return complete message string, or null if incomplete
     */
    public String extractMessage() {
        readBuffer.flip();
        int position = readBuffer.position();
        
        while (readBuffer.hasRemaining()) {
            byte b = readBuffer.get();
            
            if (b == NEWLINE) {
                // Found message terminator
                String message = messageBuffer.toString().trim();
                messageBuffer.setLength(0);
                
                // Compact buffer to remove processed data
                readBuffer.compact();
                return message.isEmpty() ? null : message;
            } else {
                messageBuffer.append((char) b);
            }
            
            // Prevent buffer overflow from malformed input
            if (messageBuffer.length() > MAX_MESSAGE_SIZE) {
                throw new IllegalArgumentException("Message too large: " + messageBuffer.length());
            }
        }
        
        // No complete message yet, compact buffer for next read
        readBuffer.compact();
        return null;
    }
    
    /**
     * Get the underlying ByteBuffer for NIO channel read operations
     * @return ByteBuffer ready for channel.read()
     */
    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }
    
    /**
     * Prepare a message for transmission by encoding as ByteBuffer
     * Adds newline terminator for protocol compliance
     * @param message the message text
     * @return ByteBuffer containing encoded message
     */
    public static ByteBuffer encodeMessage(String message) {
        String encoded = message + "\n";
        return ByteBuffer.wrap(encoded.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Parse a message into TYPE and COMMAND components
     * @param message the raw message
     * @return ParsedCommand with extracted fields
     * @throws IllegalArgumentException if format is invalid
     */
    public static ParsedCommand parseCommand(String message) throws IllegalArgumentException {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }
        
        String[] parts = message.split(String.valueOf(Constants.DELIMITER.charAt(0)));
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid message format: expected TYPE COMMAND");
        }
        
        String type = parts[0];
        String command = parts[1];
        
        String[] args = java.util.Arrays.copyOfRange(parts, 2, parts.length);
        
        return new ParsedCommand(type, command, args);
    }
    
    /**
     * Reset parser state (useful for connection reuse if needed)
     */
    public void reset() {
        messageBuffer.setLength(0);
        readBuffer.clear();
    }
    
    /**
     * Result class for parsed command
     */
    public static class ParsedCommand {
        public final String type;
        public final String command;
        public final String[] args;
        
        public ParsedCommand(String type, String command, String[] args) {
            this.type = type;
            this.command = command;
            this.args = args;
        }
        
        @Override
        public String toString() {
            return String.format("ParsedCommand{type=%s, cmd=%s, args=%d}", 
                type, command, args.length);
        }
    }
}
