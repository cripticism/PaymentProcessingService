package org.example.payment.controller;

import org.example.payment.dto.PaymentDTO;
import org.example.payment.exception.PaymentNotFoundException;
import org.example.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void testGetAllPayments_withVariousQueryParameters() throws Exception {
        // Prepare mock response
        PaymentDTO paymentDTO = new PaymentDTO(); // Assuming it has default values; set fields if needed
        Page<PaymentDTO> paymentsPage = new PageImpl<>(Collections.singletonList(paymentDTO), PageRequest.of(0, 10), 1);
        paymentDTO.setAmount(75.0);

        // Configure the service mock
        when(paymentService.getAllPayments(
                Mockito.any(Pageable.class),
                eq(50.0),  // amountGreaterThan
                eq(100.0), // amountLessThan
                eq(null),  // amountEquals
                eq(null),  // minAmount
                eq(null)   // maxAmount
        )).thenReturn(paymentsPage);

        // Perform request and verify results
        mockMvc.perform(get("/payments")
                        .param("amountGreaterThan", "50")
                        .param("amountLessThan", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists())
                .andExpect(jsonPath("$.content[0].amount", is(75.0))); // Add more checks as needed, e.g., checking payment fields
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentDTO createdPayment = new PaymentDTO(150.0, "USD", "AccountA", "AccountB");
        when(paymentService.createPayment(Mockito.any(PaymentDTO.class))).thenReturn(createdPayment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.0,\"currency\":\"USD\",\"fromAccount\":\"AccountA\",\"toAccount\":\"AccountB\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", is(150.0)))
                .andExpect(jsonPath("$.currency", is("USD")));

        verify(paymentService, times(1)).createPayment(Mockito.any(PaymentDTO.class));

    }

    @Test
    void testDeletePayment_whenPaymentExists() throws Exception {
        Long paymentId = 1L;
        doNothing().when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).deletePayment(paymentId);
    }

    @Test
    void testDeletePayment_whenPaymentNotFound() throws Exception {
        Long paymentId = 1L;
        doThrow(new PaymentNotFoundException(paymentId)).when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId))
                .andExpect(status().isNotFound());

        verify(paymentService, times(1)).deletePayment(paymentId);
    }
}
