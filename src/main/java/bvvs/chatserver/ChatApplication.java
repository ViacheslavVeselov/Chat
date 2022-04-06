package bvvs.chatserver;

import bvvs.chatserver.models.Role;
import bvvs.chatserver.models.User;
import bvvs.chatserver.repo.RoleRepository;
import bvvs.chatserver.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Iterator;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
@AllArgsConstructor
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}
}


@Component
class Initializer {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Value("${admin-pass}")
	private String adminPass;

	@EventListener(ApplicationReadyEvent.class)
	public void initialize() {
		if (!(roleRepository.existsByName("USER") && roleRepository.existsByName("ADMIN"))) {
			Iterator<Role> iterator = roleRepository.saveAll(List.of(new Role("USER"), new Role("ADMIN"))).iterator();

			iterator.next();
			Role admin = iterator.next();

			userRepository.save(User.builder().email("mykytvark@gmail.com").name("Admin").password(adminPass)
					.role(admin).build());
		}
	}
}