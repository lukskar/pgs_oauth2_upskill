package eu.lukskar.upskill.todolists.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
public class GoogleCalendarService {

    private static final String PRIMARY = "primary";
    private static final String APP_NAME = "ToDoLists OAuth2 App";

    public Event publishReminderFor(ToDoTask toRemind, String dateTime, String accessToken) throws GeneralSecurityException, IOException {
        Calendar service = calendarService(accessToken);
        String calendarTimeZone = service.calendars().get(PRIMARY).execute().getTimeZone();
        Event reminder = buildReminder(toRemind, dateTime, calendarTimeZone);

        return service.events().insert(PRIMARY, reminder).execute();
    }

    private Calendar calendarService(String accessToken) throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APP_NAME)
                .build();
    }

    private Event buildReminder(ToDoTask toRemind, String dateTime, String timeZone) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        ZonedDateTime startDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(timeZone));
        ZonedDateTime endDateTime = startDateTime.plusMinutes(30);

        EventDateTime eventStart = new EventDateTime()
                .setDateTime(new DateTime(startDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .setTimeZone(timeZone);

        EventDateTime eventEnd = new EventDateTime()
                .setDateTime(new DateTime(endDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .setTimeZone(timeZone);

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Collections.singletonList(
                        new EventReminder().setMethod("popup").setMinutes(0)
                ));

        return new Event()
                .setSummary(toRemind.getName())
                .setStart(eventStart)
                .setEnd(eventEnd)
                .setReminders(reminders);
    }
}
