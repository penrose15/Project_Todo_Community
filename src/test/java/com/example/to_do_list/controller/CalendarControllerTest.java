package com.example.to_do_list.controller;

import com.example.to_do_list.WithAuthUser;
import com.example.to_do_list.common.security.config.SecurityConfig;
import com.example.to_do_list.dto.CalendarDto;
import com.example.to_do_list.dto.todo.TodoCalendarDTO;
import com.example.to_do_list.service.CalendarService;
import com.example.to_do_list.service.UsersService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentRequest;
import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CalendarController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
public class CalendarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CalendarService calendarService;

    @MockBean
    UsersService usersService;

    @Autowired
    private Gson gson;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 7);


    @Test
    @WithAuthUser
    void getTodoByMonth() throws Exception {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        doReturn(1L)
                .when(usersService).findByEmail(anyString());

        TodoCalendarDTO todoCalendarDTO1 = TodoCalendarDTO.builder()
                .id(1L)
                .title("title")
                .endDate(LocalDate.of(2023,3,22))
                .priority(1)
                .build();
        TodoCalendarDTO todoCalendarDTO2 = TodoCalendarDTO.builder()
                .id(2L)
                .title("title")
                .endDate(LocalDate.of(2023,3,24))
                .priority(1)
                .build();

        List<TodoCalendarDTO> list = List.of(todoCalendarDTO1, todoCalendarDTO2);
        CalendarDto calendarDto = CalendarDto.builder()
                .year(2023)
                .month(3)
                .todoCalendarDTOS(list)
                .build();
        doReturn(calendarDto)
                .when(calendarService).todoMonth(anyInt(), anyInt(),anyLong());

        ResultActions actions = mockMvc.perform(
                get("/api/calendar/{year}/{month}", 2023, 3)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.year").value(2023))
                .andExpect(jsonPath("$.data.month").value(3))
                .andExpect(jsonPath("$.data.todoCalendarDTOS").isArray())
                .andDo(document("showTodoOrganizedByMonth",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("year").description("연도"),
                                parameterWithName("month").description("달")
                        ),
                        responseFields(
                                fieldWithPath("data.year").type(JsonFieldType.NUMBER).description("연도"),
                                fieldWithPath("data.month").type(JsonFieldType.NUMBER).description("달"),
                                fieldWithPath("data.todoCalendarDTOS").type(JsonFieldType.ARRAY).description("todo를 달 별로 조회"),
                                fieldWithPath("data.todoCalendarDTOS.[]id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                fieldWithPath("data.todoCalendarDTOS.[]title").type(JsonFieldType.STRING).description("todo 이름"),
                                fieldWithPath("data.todoCalendarDTOS.[]endDate").type(JsonFieldType.STRING).description("todo 내용"),
                                fieldWithPath("data.todoCalendarDTOS.[]priority").type(JsonFieldType.NUMBER).description("todo 우선순위")
                        )
                        ));
    }
}
