package exampletestsuite;

import java.util.Map;

import static utility.services.FileReaderService.getMap;

public interface HelpMethods {

    String searchRequestText = "Text - Wikipedia";

    Map<String, String> texts = getMap("properties/texts.txt");
}
