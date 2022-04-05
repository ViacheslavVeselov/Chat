package bvvs.chatserver.repo;

import bvvs.chatserver.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

//@RepositoryRestResource(path = "users", collectionResourceRel = "userz")
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
