package bvvs.chatserver.repo;

import bvvs.chatserver.models.Chat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
//@RepositoryRestResource(path = "chats", collectionResourceRel = "chat")
public interface ChatRepository extends PagingAndSortingRepository<Chat, UUID> {
}
