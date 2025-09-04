package net.sso.ssoservice;

import org.springframework.boot.SpringApplication;

public class TestSsoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SsoServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
