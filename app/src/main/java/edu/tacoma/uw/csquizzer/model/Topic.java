package edu.tacoma.uw.csquizzer.model;

public class Topic {
    private int TopicId;
    private String TopicDescription;

    public Topic(int topicId, String topicDescription) {
        TopicId = topicId;
        TopicDescription = topicDescription;
    }

    public int getTopicId() {
        return TopicId;
    }

    public void setTopicId(int topicId) {
        TopicId = topicId;
    }

    public String getTopicDescription() {
        return TopicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        TopicDescription = topicDescription;
    }
}
