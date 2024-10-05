package org.ticketplatform.java;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturnNotFound() throws Exception {
		
		mockMvc.perform(get("/api/tickets/id/999")).andExpect(status().isNotFound()).andDo(print());
		
	}
	
	@Test
	void shouldReturnRightId() throws Exception {
		
		mockMvc.perform(get("/api/tickets/id/1")).
		andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1")).andDo(print());
		
	}
	
	@Test
	void shouldReturnOkForTicketStatus() throws Exception {
		
		mockMvc.perform(get("/api/tickets/status/da fare")).andExpect(status().isOk());
		
	}
	
	@Test
	void shouldReturnOkForTicketCategory() throws Exception {
		
		mockMvc.perform(get("/api/tickets/category/Gestionale")).andExpect(status().isOk());
		
	}
	
}
