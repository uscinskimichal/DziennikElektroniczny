package message;

public class Message {

    private int id;
    private String sender;
    private String receiver;
    private String topic;
    private String message;
    private String senderStatus;
    private String receiverStatus;
    private String data;


    public Message(int id, String sender, String receiver, String topic, String message, String senderStatus, String receiverStatus, String data) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.topic = topic;
        this.message = message;
        this.senderStatus = senderStatus;
        this.receiverStatus = receiverStatus;
        this.data = data;
    }

    int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderStatus() {
        return senderStatus;
    }

    public String getReceiverStatus() {
        return receiverStatus;
    }

    public String getData() {
        return data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderStatus(String senderStatus) {
        this.senderStatus = senderStatus;
    }

    public void setReceiverStatus(String receiverStatus) {
        this.receiverStatus = receiverStatus;
    }

    public void setData(String data) {
        this.data = data;
    }

}
