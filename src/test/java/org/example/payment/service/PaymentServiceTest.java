package org.example.payment.service;

import org.example.payment.dto.PaymentDTO;
import org.example.payment.exception.InvalidPaymentException;
import org.example.payment.exception.PaymentNotFoundException;
import org.example.payment.mapper.PaymentMapper;
import org.example.payment.model.Payment;
import org.example.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment(1L, 100.0, "USD", "Account1", "Account2", null);
        Payment payment2 = new Payment(2L, 200.0, "EUR", "Account3", "Account4", null);

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment1, payment2));
        when(paymentMapper.toDto(payment1)).thenReturn(new PaymentDTO(100.0, "USD", "Account1", "Account2"));
        when(paymentMapper.toDto(payment2)).thenReturn(new PaymentDTO(200.0, "EUR", "Account3", "Account4"));

        List<PaymentDTO> payments = paymentService.getAllPayments();

        assertEquals(2, payments.size());
        verify(paymentRepository, times(1)).findAll();
        verify(paymentMapper, times(2)).toDto(any(Payment.class));
    }

    @Test
    void testCreatePayment_withValidData() {
        PaymentDTO paymentDTO = new PaymentDTO(150.0, "USD", "AccountA", "AccountB");
        Payment payment = new Payment(null, 150.0, "USD", "AccountA", "AccountB", null);
        Payment savedPayment = new Payment(1L, 150.0, "USD", "AccountA", "AccountB", null);

        when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(savedPayment);
        when(paymentMapper.toDto(savedPayment)).thenReturn(new PaymentDTO(150.0, "USD", "AccountA", "AccountB"));

        PaymentDTO result = paymentService.createPayment(paymentDTO);

        assertEquals(paymentDTO.getAmount(), result.getAmount());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testCreatePayment_withInvalidAmount() {
        PaymentDTO paymentDTO = new PaymentDTO(-100.0, "USD", "AccountA", "AccountB");

        assertThrows(InvalidPaymentException.class, () -> paymentService.createPayment(paymentDTO));
    }

    @Test
    void testDeletePayment_whenPaymentExists() {
        Long paymentId = 1L;
        when(paymentRepository.existsById(paymentId)).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(paymentId);

        paymentService.deletePayment(paymentId);

        verify(paymentRepository, times(1)).deleteById(paymentId);
    }

    @Test
    void testDeletePayment_whenPaymentNotFound() {
        Long paymentId = 1L;
        when(paymentRepository.existsById(paymentId)).thenReturn(false);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(paymentId));
    }
}

