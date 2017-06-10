package ch.bernmobil.vibe.realtimedata;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.shared.mockdata.JourneyMapperMockData;
import org.apache.log4j.Logger;
import org.mockito.Mockito;


public class ImportRunnerMock {
    ImportRunner mock;
    Logger logger = Logger.getLogger(ImportRunnerMock.class);

    public ImportRunnerMock(){
        mock = Mockito.mock(ImportRunner.class);
        configureMock();
    }

    protected void configureMock() {
        when(mock.convertToScheduleUpdateInformation(any(), any())).thenCallRealMethod();
        when(mock.extractScheduleUpdateInformation(anyList())).thenCallRealMethod();
        when(mock.convert(anyList())).thenCallRealMethod();

        try{
            doCallRealMethod().when(mock).run();
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }


    }

    public ImportRunner getMock() {
        return mock;
    }

}
