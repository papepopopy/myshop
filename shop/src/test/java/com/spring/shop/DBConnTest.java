package com.spring.shop;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;



@SpringBootTest
@Log4j2
public class DBConnTest {
	@Autowired
	private DataSource ds;
	
	@Test@DisplayName("DB연결 테스트")
	public void testConnection() throws SQLException {
		
		@Cleanup
		Connection con = ds.getConnection();
		log.info(con);
		
		Assertions.assertNotNull(con);
	}
}
