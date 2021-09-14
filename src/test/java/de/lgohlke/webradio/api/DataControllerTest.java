package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataControllerTest {
    private final DataService service = mock(DataService.class);
    private final DataController controller = new DataController(service);

    @Test
    @SneakyThrows
    void shouldCallDataServiceOnlyOnce() {
        when(service.fetchStationInfo("test")).thenReturn(new StationInfo());

        controller.getStationInfo("test");
        controller.getStationInfo("test");

        verify(service, times(1)).fetchStationInfo("test");
    }
}
