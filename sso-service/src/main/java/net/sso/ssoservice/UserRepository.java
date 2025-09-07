package net.sso.ssoservice;

import org.commons.auth.UserDetailsImpl;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDetailsImpl, Long> {

}
