package bvvs.chatserver.repo;

import bvvs.chatserver.models.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "roles", collectionResourceRel = "role")
public interface RoleRepository extends PagingAndSortingRepository<Role, UUID> {
    boolean existsByName(String name);
}
