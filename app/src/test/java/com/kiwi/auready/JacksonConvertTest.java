package com.kiwi.auready;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwi.auready.data.Friend;
import com.kiwi.auready.data.TaskHead;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by kiwi on 12/26/16.
 */

public class JacksonConvertTest {

    @Test
    public void givenJsonOfList_WhenDeserializing_thenCorrect()
            throws IOException {
        TaskHead taskHead = new TaskHead("title", 0, R.color.color_picker_default_color);
//        String json = taskHead.getMembersString();

        String json = "{\"email\":\"email1\",\"name\":\"name1\",\"id\":\"48960d3b-9866-4272-b3dd-403a31886d37\"}";

        ObjectMapper mapper = new ObjectMapper();

        List<Friend> friends =
                mapper.reader()
                .forType(new TypeReference<List<Friend>>() {})
                        .readValue(json);

//        assertEquals(MEMBERS.get(0).getName(), friends.get(0).getName());
    }
}
