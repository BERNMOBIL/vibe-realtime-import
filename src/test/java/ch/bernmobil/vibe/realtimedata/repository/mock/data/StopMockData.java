package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.shared.entitiy.Stop;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StopMockData {
    private static List<Stop> dataSource;

    private static UUID[] ids = {
        UUID.fromString("c8f1191b-9316-4834-a384-262a63fdc7f0"),
        UUID.fromString("115a3a10-7744-4226-adf3-8c35ad6fac0a"),
        UUID.fromString("6dd26aaa-3120-4d3a-b492-6f8d2cf7856e")
    };

    private static String[] names = {
        "Selzach, Schildmatte",
        "Frutigen, Hohstalden",
        "Frutigen, Innerbräschgen"
    };

    private static UUID[] areas = {
        UUID.fromString("8fc5f4f3-be6d-40be-8010-47e640e21158"),
        UUID.fromString("4d7b2db0-0af5-4c22-b46e-2984b710405d"),
        UUID.fromString("0ca7771d-1f23-42c8-8d39-914b193f945c")
    };


    private static Stop create(int index) {
        return new Stop(ids[index], names[index], areas[index]);
    }


    public static List<Stop> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();

            for(int i = 0; i < ids.length; i++) {
                dataSource.add(create(i));
            }
        }

        return dataSource;
    }

}
