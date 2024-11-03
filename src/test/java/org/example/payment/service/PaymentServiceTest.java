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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    private Pageable pageable;
    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pageable = PageRequest.of(0, 10);

        // Create sample Payment and PaymentDTO
        payment = new Payment();
        payment.setAmount(150.0);
        paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(150.0);

        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentDTO);
    }

    @Test
    public void testGetAllPayments_withAmountGreaterThan() {
        // Arrange
        Double amountGreaterThan = 100.0;
        Page<Payment> paymentsPage = new PageImpl<>(List.of(payment));
        when(paymentRepository.findByAmountGreaterThan(amountGreaterThan, pageable)).thenReturn(paymentsPage);

        // Act
        Page<PaymentDTO> result = paymentService.getAllPayments(pageable, amountGreaterThan, null, null, null, null);

        // Assert
        verify(paymentRepository, times(1)).findByAmountGreaterThan(amountGreaterThan, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(paymentDTO.getAmount(), result.getContent().get(0).getAmount());
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

