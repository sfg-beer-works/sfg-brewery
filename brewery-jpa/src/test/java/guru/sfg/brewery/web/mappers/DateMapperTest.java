package guru.sfg.brewery.web.mappers;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class DateMapperTest {

    public static final String TIMESTAMP_STRING = "2019-01-01 01:02:03.0";
    public static final String OFFSET_STRING = "2019-01-01T01:02:03Z";

    DateMapper dateMapper = new DateMapper();

    @Test
    void asOffsetDateTime() {
        //given
        Timestamp ts = Timestamp.valueOf(TIMESTAMP_STRING);
        //when
        OffsetDateTime offsetDateTime = dateMapper.asOffsetDateTime(ts);
        //then
        assertThat(OFFSET_STRING).isEqualTo(offsetDateTime.withOffsetSameLocal(ZoneOffset.UTC).toString());

    }

    @Test
    void asTimestamp() {
        //given
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(OFFSET_STRING);
        //when
        Timestamp ts = dateMapper.asTimestamp(offsetDateTime);
        //then
        assertThat(TIMESTAMP_STRING).isEqualTo(ts.toString());

    }
}