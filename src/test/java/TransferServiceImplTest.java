import com.demo.example.mstransfercontrol.dto.TransferRequest;
import com.demo.example.mstransfercontrol.dto.TransferResponse;
import com.demo.example.mstransfercontrol.entity.Account;
import com.demo.example.mstransfercontrol.mapper.TransferMapper;
import com.demo.example.mstransfercontrol.mapper.impl.TransferMapperImpl;
import com.demo.example.mstransfercontrol.repository.AccountRepository;
import com.demo.example.mstransfercontrol.repository.TransferRepository;
import com.demo.example.mstransfercontrol.service.impl.TransferServiceImpl;
import com.demo.example.mstransfercontrol.entity.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @Spy
    private TransferMapper transferMapper = new TransferMapperImpl();

    @InjectMocks
    private TransferServiceImpl transferService;

    // AccountRepository Test
    @Test
    void save_CuandoCrea() {
        // Arrange
        Account account = new Account(3L, "001-100003", "102", "ACTIVE");

        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account result = accountRepository.save(account);

        // Assert
        assertEquals(account, result);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void findByAccountNumber_CuandoRetorna(){
        // Arrange
        String accountNumber = "001-100001";
        Account mockAccount = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(mockAccount));
        // Act
        Optional<Account> result = accountRepository.findByAccountNumber(accountNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockAccount, result.get());
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void save_CuandoActualiza() {
        // Arrange
        Account account = new Account(2L, "001-100002", "101", "BLOCKED");

        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account result = accountRepository.save(account);

        // Assert
        assertEquals("BLOCKED", result.getStatus());
        assertEquals(account, result);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void delete_CuandoElimina() {
        // Arrange
        Account account = new Account(4L, "001-100004", "103", "ACTIVE");

        doNothing().when(accountRepository).delete(account);

        // Act
        accountRepository.delete(account);

        // Assert
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void findByAccountNumber_CuandoNoRetorna() {
        // Arrange
        String accountNumber = "999-999999";

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        // Act
        Optional<Account> result = accountRepository.findByAccountNumber(accountNumber);

        // Assert
        assertTrue(result.isEmpty());
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void findAll_CuandoRetorna() {
        // Arrange
        Account account1 = new Account(1L, "001-100001", "100", "ACTIVE");
        Account account2 = new Account(2L, "001-100002", "101", "ACTIVE");
        List<Account> expectedAccounts = Arrays.asList(account1, account2);

        when(accountRepository.findAll()).thenReturn(expectedAccounts);

        // Act
        List<Account> result = accountRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedAccounts, result);
        verify(accountRepository, times(1)).findAll();
    }
    // --------


    // TransferRepository Test
    @Test
    void transferSave_CuandoCrea() {
        // Arrange
        Transfer transfer = new Transfer(1L, "001-100001", "001-200001", "100", "successful",
                new BigDecimal("500.00"), LocalDateTime.now());

        when(transferRepository.save(transfer)).thenReturn(transfer);

        // Act
        Transfer result = transferRepository.save(transfer);

        // Assert
        assertEquals(transfer, result);
        verify(transferRepository, times(1)).save(transfer);
    }

    @Test
    void transferFindById_CuandoRetorna() {
        // Arrange
        Integer id = 1;
        Transfer transfer = new Transfer(1L, "001-100001", "001-200001", "100", "successful",
                new BigDecimal("500.00"), LocalDateTime.now());

        when(transferRepository.findById(id)).thenReturn(Optional.of(transfer));

        // Act
        Optional<Transfer> result = transferRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(transfer, result.get());
        verify(transferRepository, times(1)).findById(id);
    }

    @Test
    void transferSave_CuandoActualiza() {
        // Arrange
        Transfer transfer = new Transfer(2L, "001-100001", "001-200002", "100", "rejected",
                new BigDecimal("250.00"), LocalDateTime.now());

        when(transferRepository.save(transfer)).thenReturn(transfer);

        // Act
        Transfer result = transferRepository.save(transfer);

        // Assert
        assertEquals("rejected", result.getStatus());
        assertEquals(transfer, result);
        verify(transferRepository, times(1)).save(transfer);
    }

    @Test
    void transferDelete_CuandoElimina() {
        // Arrange
        Transfer transfer = new Transfer(3L, "001-100001", "001-200003", "100", "successful",
                new BigDecimal("150.00"), LocalDateTime.now());

        doNothing().when(transferRepository).delete(transfer);

        // Act
        transferRepository.delete(transfer);

        // Assert
        verify(transferRepository, times(1)).delete(transfer);
    }

    @Test
    void transferFindById_CuandoNoRetorna() {
        // Arrange
        Integer id = 999;

        when(transferRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Transfer> result = transferRepository.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(transferRepository, times(1)).findById(id);
    }

    // He investigado sobre MethodSource y lo he utilizado porque veo que es más optimo para este caso.
    @ParameterizedTest
    @MethodSource("transferListProvider")
    void transferFindAll_CuandoRetorna(List<Transfer> transfers) {
        // Arrange
        when(transferRepository.findAll()).thenReturn(transfers);

        // Act
        List<Transfer> result = transferRepository.findAll();

        // Assert
        assertEquals(transfers, result);
        verify(transferRepository, times(1)).findAll();
    }

    private static Stream<Arguments> transferListProvider() {
        Transfer transfer1 = new Transfer(1L, "001-100001", "001-200001", "100", "successful",
                new BigDecimal("500.00"), LocalDateTime.now());
        Transfer transfer2 = new Transfer(2L, "001-100001", "001-200002", "100", "successful",
                new BigDecimal("1200.50"), LocalDateTime.now());
        Transfer transfer3 = new Transfer(3L, "001-100002", "001-200003", "101", "rejected",
                new BigDecimal("300.00"), LocalDateTime.now());

        return Stream.of(
                Arguments.of(List.of(transfer1)),
                Arguments.of(List.of(transfer1, transfer2)),
                Arguments.of(List.of(transfer1, transfer2, transfer3))
        );
    }


    // Capa de Negocio Test
    @Test
    void validateAndTransfer_CuandoCuentaEsActivaYTransferValido() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        when(transferRepository.totalTransferredToday(eq("001-100001"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.ZERO);

        // Act
        TransferResponse response = transferService.validateAndTransfer(request);

        // Assert
        assertEquals("APPROVED", response.getStatus());
        assertEquals("001-100001", response.getSourceAccount());
        assertEquals("001-200001", response.getTargetAccount());
        assertEquals(new BigDecimal("100.00"), response.getAmount());

        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoCuentaNoExiste() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("999-000001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        when(accountRepository.findByAccountNumber("999-000001"))
                .thenReturn(Optional.empty());
        // Act

        // Assert
        assertThrows(NoSuchElementException.class,
                () -> transferService.validateAndTransfer(request));
    }

    @Test
    void validateAndTransfer_CuandoCuentaNoEstaActiva() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-200005")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        Account account = new Account(5L, "001-200005", "104", "BLOCKED");

        when(accountRepository.findByAccountNumber("001-200005"))
                .thenReturn(Optional.of(account));

        // Act
        // Assert
        assertThrows(IllegalStateException.class,
                () -> transferService.validateAndTransfer(request));
        verify(accountRepository, times(1)).findByAccountNumber("001-200005");
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoMontoEsNulo() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(null)
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> transferService.validateAndTransfer(request));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoMontoEsCero() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(BigDecimal.ZERO)
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> transferService.validateAndTransfer(request));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoMontoEsNegativo() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("-10.00"))
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> transferService.validateAndTransfer(request));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoLimiteDiarioExcedido() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        when(transferRepository.totalTransferredToday(eq("001-100001"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new BigDecimal("17000.00"));

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> transferService.validateAndTransfer(request));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoLimiteDiarioAlcanzaMaximo() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        Account account = new Account(1L, "001-100001", "100", "ACTIVE");

        when(accountRepository.findByAccountNumber("001-100001"))
                .thenReturn(Optional.of(account));

        when(transferRepository.totalTransferredToday(eq("001-100001"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new BigDecimal("9900.00"));

        // Act
        TransferResponse response = transferService.validateAndTransfer(request);

        // Assert
        assertEquals("APPROVED", response.getStatus());
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void validateAndTransfer_CuandoCuentaNoExiste_noDebeInteractuarConTransferRepository() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("999-000001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("100.00"))
                .build();

        when(accountRepository.findByAccountNumber("999-000001"))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(NoSuchElementException.class,
                () -> transferService.validateAndTransfer(request));

        // Assert
        verify(accountRepository, times(1)).findByAccountNumber("999-000001");
        verify(transferRepository, never()).save(any(Transfer.class));
    }

}
