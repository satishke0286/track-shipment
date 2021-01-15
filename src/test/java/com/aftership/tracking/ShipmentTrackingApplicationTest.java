package com.aftership.tracking;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ShipmentTrackingApplicationTest {

	@Test
	public void contextLoads() {
		assertNotNull(ShipmentTrackingApplicationTest.class);
	}
}

