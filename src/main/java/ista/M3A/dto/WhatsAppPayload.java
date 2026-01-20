package ista.M3A.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppPayload {

    private List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    // --- CLASES INTERNAS ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        private String id;
        private List<Change> changes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Change> getChanges() {
            return changes;
        }

        public void setChanges(List<Change> changes) {
            this.changes = changes;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Change {
        private Value value;
        private String field;

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        private String messaging_product;
        private List<Message> messages;
        private List<Contact> contacts; // ✅ ESTO FALTABA ANTES

        public String getMessaging_product() {
            return messaging_product;
        }

        public void setMessaging_product(String messaging_product) {
            this.messaging_product = messaging_product;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }
        
        public List<Contact> getContacts() {
            return contacts;
        }
        
        public void setContacts(List<Contact> contacts) {
            this.contacts = contacts;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contact {
        private Profile profile;
        private String wa_id;
        
        public Profile getProfile() {
            return profile;
        }
        
        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public String getWa_id() {
            return wa_id;
        }

        public void setWa_id(String wa_id) {
            this.wa_id = wa_id;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        private String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String from;
        private String id;
        private String timestamp;
        private String type;
        private Text text;

        @JsonProperty("interactive")
        private Interactive interactive;

        // Getters y Setters
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public Interactive getInteractive() {
            return interactive;
        }

        public void setInteractive(Interactive interactive) {
            this.interactive = interactive;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Text {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Interactive {
        private Reply button_reply;

        public Reply getButton_reply() {
            return button_reply;
        }

        public void setButton_reply(Reply button_reply) {
            this.button_reply = button_reply;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reply {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}