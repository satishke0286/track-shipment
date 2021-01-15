package com.aftership.tracking.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.service.ShipmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShipmentTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @Test
    public void create_shipmentTracking() throws Exception {
        ShipmentModel shipmentModel = getShipmentModel();
        when(shipmentService.createTracking(any(ShipmentModel.class))).thenReturn(shipmentModel);

        MvcResult result = mockMvc.perform(post("/tracking", any(ShipmentModel.class))
                .contentType(MediaType.APPLICATION_JSON).content(convertObjectToString(shipmentModel)))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
    }

    private String convertObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    private ShipmentModel getShipmentModel() {
        return ShipmentModel.builder()
                    .id("6a1e9bb8-8e30-11ea-bc55-0242ac130003")
                    .courierCode("FedEx")
                    .trackingNumber("9374889676091297266845")
                    .currentStatus("In Transit")
                    .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                    .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                    .build();
    }

}
