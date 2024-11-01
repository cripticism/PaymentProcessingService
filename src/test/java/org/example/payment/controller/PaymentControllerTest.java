package org.example.payment.controller;

import org.example.payment.dto.PaymentDTO;
import org.example.payment.exception.PaymentNotFoundException;
import org.example.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
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
    void testGetAllPayments() throws Exception {
        PaymentDTO payment1 = new PaymentDTO(100.0, "USD", "Account1", "Account2");
        PaymentDTO payment2 = new PaymentDTO(200.0, "EUR", "Account3", "Account4");
        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(payment1, payment2));

        mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", is(100.0)))
                .andExpect(jsonPath("$[1].currency", is("EUR")));
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO(150.0, "USD", "AccountA", "AccountB");
        PaymentDTO createdPayment = new PaymentDTO(150.0, "USD", "AccountA", "AccountB");
        when(paymentService.createPayment(Mockito.any(PaymentDTO.class))).thenReturn(createdPayment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.0,\"currency\":\"USD\",\"fromAccount\":\"AccountA\",\"toAccount\":\"AccountB\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", is(150.0)))
                .andExpect(jsonPath("$.currency", is("USD")));
    }

    @Test
    void testDeletePayment_whenPaymentExists() throws Exception {
        Long paymentId = 1L;
        doNothing().when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePayment_whenPaymentNotFound() throws Exception {
        Long paymentId = 1L;
        doThrow(new PaymentNotFoundException(paymentId)).when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId))
                .andExpect(status().isNotFound());
    }
}
