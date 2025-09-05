package net.sso.ssoservice;

import org.auth.
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDetailsImpl, Long> {
}
