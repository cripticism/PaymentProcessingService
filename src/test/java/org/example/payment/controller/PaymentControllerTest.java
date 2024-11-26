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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @WithMockUser
    void testGetAllPayments_withVariousQueryParameters() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        Page<PaymentDTO> paymentsPage = new PageImpl<>(Collections.singletonList(paymentDTO), PageRequest.of(0, 10), 1);
        paymentDTO.setAmount(75.0);

        when(paymentService.getAllPayments(
                Mockito.any(Pageable.class),
                eq(50.0),
                eq(100.0),
                eq(null),
                eq(null),
                eq(null)
        )).thenReturn(paymentsPage);

        mockMvc.perform(get("/payments")
                        .param("amountGreaterThan", "50")
                        .param("amountLessThan", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists())
                .andExpect(jsonPath("$.content[0].amount", is(75.0)));
    }

    @Test
    @WithMockUser
    void testCreatePayment() throws Exception {
        PaymentDTO createdPayment = new PaymentDTO(150.0, "USD", "AccountA", "AccountB");
        when(paymentService.createPayment(Mockito.any(PaymentDTO.class))).thenReturn(createdPayment);

        mockMvc.perform(post("/payments")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.0,\"currency\":\"USD\",\"fromAccount\":\"AccountA\",\"toAccount\":\"AccountB\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", is(150.0)))
                .andExpect(jsonPath("$.currency", is("USD")));

        verify(paymentService, times(1)).createPayment(Mockito.any(PaymentDTO.class));

    }

    @Test
    @WithMockUser
    void testDeletePayment_whenPaymentExists() throws Exception {
        Long paymentId = 1L;
        doNothing().when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId).with(csrf().asHeader()))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).deletePayment(paymentId);
    }

    @Test
    @WithMockUser
    void testDeletePayment_whenPaymentNotFound() throws Exception {
        Long paymentId = 1L;
        doThrow(new PaymentNotFoundException(paymentId)).when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/payments/{id}", paymentId).with(csrf().asHeader()))
                .andExpect(status().isNotFound());

        verify(paymentService, times(1)).deletePayment(paymentId);
    }
}
