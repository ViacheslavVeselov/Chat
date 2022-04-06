package bvvs.chatserver.repo;

import bvvs.chatserver.models.ChatMessage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "messages", collectionResourceRel = "message")
public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, UUID> {

}
