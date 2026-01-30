package com.example.demo.repository;

import java.util.List;

public interface WishRepository {
	void addRecipientsBatch(int messageId, List<String> wishersList);
}
