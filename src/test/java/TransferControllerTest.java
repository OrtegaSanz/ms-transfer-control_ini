import com.demo.example.mstransfercontrol.controller.TransferController;
import com.demo.example.mstransfercontrol.service.TransferService;
import com.demo.example.mstransfercontrol.dto.TransferRequest;
import com.demo.example.mstransfercontrol.dto.TransferResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @Test
    void transfer_CuandoSeInvocaEndpoint_DelegaEnService() {
        // Arrange
        Map<String, String> headers = new HashMap<>();
        headers.put("x-trace-id", "trace-123");

        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("150.00"))
                .build();

        TransferResponse expectedResponse = TransferResponse.builder()
                .status("APPROVED")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("150.00"))
                .build();

        when(transferService.validateAndTransfer(request)).thenReturn(expectedResponse);

        // Act
        TransferResponse response = transferController.transfer(headers, request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(transferService, times(1)).validateAndTransfer(request);
    }

    @Test
    void transfer_CuandoHeadersVacios_DelegaEnService() {
        // Arrange
        Map<String, String> headers = new HashMap<>();
        TransferRequest request = TransferRequest.builder()
                .customerId("100")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("50.00"))
                .build();

        TransferResponse expectedResponse = TransferResponse.builder()
                .status("APPROVED")
                .sourceAccount("001-100001")
                .targetAccount("001-200001")
                .amount(new BigDecimal("50.00"))
                .build();

        when(transferService.validateAndTransfer(request)).thenReturn(expectedResponse);

        // Act
        TransferResponse response = transferController.transfer(headers, request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(transferService, times(1)).validateAndTransfer(request);
    }





}
