package ru.yandex.practicum.filmorate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(duration.toSeconds());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        return Duration.ofSeconds(Integer.parseInt(jsonReader.nextString()));
    }
}
