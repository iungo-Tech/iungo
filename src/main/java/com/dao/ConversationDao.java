package com.dao;

import com.model.Conversation;
import com.model.Message;

import java.util.List;

public interface ConversationDao {

    void addConversation(Conversation conversation);

    List<Conversation> getAllConversations();

    Conversation getWithMessages(String id);
}