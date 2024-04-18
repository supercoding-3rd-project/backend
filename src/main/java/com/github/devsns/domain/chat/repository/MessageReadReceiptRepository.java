package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.dto.MessageReadReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageReadReceiptRepository extends JpaRepository<MessageReadReceipt, Long> {
   //메세지 읽음 처리
    List<MessageReadReceipt> findByMessageId(Long messageId);

}
