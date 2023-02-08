package com.example.to_do_list.controller;

import com.example.to_do_list.WithAuthUser;
import com.example.to_do_list.common.security.config.SecurityConfig;
import com.example.to_do_list.common.security.jwt.JwtTokenizer;
import com.example.to_do_list.dto.todo.*;
import com.example.to_do_list.service.TodoService;
import com.example.to_do_list.service.UsersService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentRequest;
import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TodoController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TodoService todoService;
    @MockBean
    private UsersService usersService;
    @Autowired
    private Gson gson;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 02, 9);

    @Test
    @WithAuthUser
    public void postTest() throws Exception {
        TodoSaveDto todoSaveDto = TodoSaveDto.builder()
                .title("title")
                .content("content")
                .expose("PUBLIC")
                .endDate("2023-02-10")
                .build();
        String content = gson.toJson(todoSaveDto);

        given(usersService.save())
                .willReturn(1L);

        given(usersService.findByEmail(Mockito.anyString()))
                .willReturn(1L);
        given(todoService.save(Mockito.any(TodoSaveDto.class),anyLong()))
                .willReturn(1L);

        ResultActions actions = mockMvc.perform(
                post("/api/todo/posts")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        actions.andExpect(status().isCreated())
                .andDo(document("post-todo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("본문"),
                                        fieldWithPath("expose").type(JsonFieldType.STRING).description("공개여부"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).description("우선순위"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("마감 날자")
                                )
                        )
                ));
    }

    @Test
    @WithAuthUser
    void Todo_get() throws Exception {
        long todoId = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TodoResponseDto todoResponseDto = TodoResponseDto.builder()
                .title("title")
                .content("content")
                .status(true)
                .expose("PUBLIC")
                .endDate(LocalDate.now().format(formatter))
                .build();

        given(usersService.save())
                .willReturn(1L);

        given(usersService.findByEmail(Mockito.anyString()))
                .willReturn(1L);
        given(todoService.findById(todoId))
                .willReturn(todoResponseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/todo/posts/{id}", todoId)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(todoResponseDto.getTitle()))
                .andExpect(jsonPath("$.content").value(todoResponseDto.getContent()))
                .andExpect(jsonPath("$.status").value(todoResponseDto.isStatus()))
                .andExpect(jsonPath("$.expose").value(todoResponseDto.getExpose()))
                .andExpect(jsonPath("$.endDate").value(todoResponseDto.getEndDate()))
                .andDo(document("todo-get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT AccessToken"),
                                headerWithName("Refresh").description("JWT RefreshToken")
                        ),
                        pathParameters(
                                parameterWithName("id").description("todo 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("todo제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("todo본문"),
                                        fieldWithPath("status").type(JsonFieldType.BOOLEAN).description("실행/미실행"),
                                        fieldWithPath("expose").type(JsonFieldType.STRING).description("공개/비공개"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("마감날짜")
                                )
                        )));

    }

    @Test
    @WithAuthUser
    void todo_patch() throws Exception {
        TodoUpdateDto todoUpdateDto = TodoUpdateDto.builder()
                .title("patch title")
                .content("patch content")
                .status(true)
                .expose("")
                .endDate("2023-02-15")
                .build();
        String content = gson.toJson(todoUpdateDto);
        given(usersService.findByEmail(Mockito.anyString()))
                .willReturn(1L);
        given(todoService.update(Mockito.anyLong(), Mockito.any(TodoUpdateDto.class), Mockito.anyLong()))
                .willReturn(1L);

        ResultActions actions = mockMvc.perform(
                patch("/api/todo/posts/{id}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("todo-patch",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT AccessToken"),
                                headerWithName("Refresh").description("JWT RefreshToken")
                        ),
                        pathParameters(
                                parameterWithName("id").description("todo 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("todo제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("todo본문"),
                                        fieldWithPath("status").type(JsonFieldType.BOOLEAN).description("실행/미실행"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).description("우선순위"),
                                        fieldWithPath("expose").type(JsonFieldType.STRING).description("공개/비공개"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("마감날짜")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void todo_done() throws Exception {
        Long usersId = 1L;
        given(usersService.findByEmail(Mockito.anyString()))
                .willReturn(usersId);
        given(todoService.changeStatus(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(1L);

        ResultActions actions = mockMvc.perform(
                get("/api/todo/posts/done/{id}",1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("todo-done",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT AccessToken"),
                                headerWithName("Refresh").description("JWT RefreshToken")
                                ),
                        pathParameters(
                                parameterWithName("id").description("todo식별자")
                        )
                        ));

    }

    @Test
    @WithAuthUser
    void todo_today() throws Exception {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        int page = 1;
        int size = 10;

        usersService.save();

        TodoSaveDto todoSaveDto1 = TodoSaveDto.builder()
                .title("title1")
                .content("content1")
                .endDate("2023-02-15")
                .expose("PUBLIC")
                .build();
        todoService.save(todoSaveDto1, 1L);
        TodoSaveDto todoSaveDto2 = TodoSaveDto.builder()
                .title("title2")
                .content("content2")
                .endDate("2023-02-15")
                .expose("PUBLIC")
                .build();
        todoService.save(todoSaveDto2, 1L);

        TodoResponsesDto todoResponsesDto1 = new TodoResponsesDto(1L, "title1",true);
        TodoResponsesDto todoResponsesDto2 = new TodoResponsesDto(2L, "title2",false);


        List<TodoResponsesDto> todoResponsesDtos = List.of(todoResponsesDto1, todoResponsesDto2);

        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.Direction.DESC, "todoId");
        Page<TodoResponsesDto> todoResponsesDtoPage = new PageImpl<>(todoResponsesDtos,pageRequest, 2);


        String content = gson.toJson(todoResponsesDtos);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(todoResponsesDtoPage)
                .when(todoService).findByDate(anyInt(), anyInt(), eq(LocalDate.now(fixedClock)), anyLong());

        ResultActions actions = mockMvc.perform(
                get("/api/todo/posts/today")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .param("page","1")
                        .param("size","10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.pageInfo.size").value(10))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))
                .andDo(document("todo-today",
                                getDocumentRequest(),
                                getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("Basic auth credentials")),
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("size").description("페이지 당 크기")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("todo-list"),
                                        fieldWithPath("data.[]id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                        fieldWithPath("data.[]title").type(JsonFieldType.STRING).description("todo 제목"),
                                        fieldWithPath("data.[]status").type(JsonFieldType.BOOLEAN).description("todo 실행/미실행"),
                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("page"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("size"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("totalPages")
                                )
                        )));
    }

    @Test
    @WithAuthUser
    void todo_days() throws Exception {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        int page = 1;
        int size = 10;

        usersService.save();

        TodoSaveDto todoSaveDto1 = TodoSaveDto.builder()
                .title("title1")
                .content("content1")
                .endDate("2023-02-15")
                .expose("PUBLIC")
                .build();
        todoService.save(todoSaveDto1, 1L);
        TodoSaveDto todoSaveDto2 = TodoSaveDto.builder()
                .title("title2")
                .content("content2")
                .endDate("2023-02-15")
                .expose("PUBLIC")
                .build();
        todoService.save(todoSaveDto2, 1L);

        TodoResponsesDto todoResponsesDto1 = new TodoResponsesDto(1L, "title1",true);
        TodoResponsesDto todoResponsesDto2 = new TodoResponsesDto(2L, "title2",false);


        List<TodoResponsesDto> todoResponsesDtos = List.of(todoResponsesDto1, todoResponsesDto2);

        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.Direction.DESC, "todoId");
        Page<TodoResponsesDto> todoResponsesDtoPage = new PageImpl<>(todoResponsesDtos,pageRequest, 2);

        String content = gson.toJson(todoResponsesDtos);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(todoResponsesDtoPage)
                .when(todoService).findByDate(anyInt(), anyInt(), eq(LocalDate.now(fixedClock)), anyLong());

        ResultActions actions = mockMvc.perform(
                get("/api/todo/posts/days")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .param("page","1")
                        .param("size","10")
                        .param("date","2023-02-09")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.pageInfo.size").value(10))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))
                .andDo(document("todo-find-days",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken")),
                        requestHeaders(headerWithName("Refresh").description("refreshToken")),
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("size").description("페이지 당 크기"),
                                parameterWithName("date").description("검색하고자 하는 날짜")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("todo-list"),
                                        fieldWithPath("data.[]id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                        fieldWithPath("data.[]title").type(JsonFieldType.STRING).description("todo 제목"),
                                        fieldWithPath("data.[]status").type(JsonFieldType.BOOLEAN).description("todo 실행/미실행"),
                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("page"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("size"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("totalPages")
                                )
                        )));
    }

    @Test
    @WithAuthUser
    void delete_todo() throws Exception {
        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doNothing()
                .when(todoService).deleteTodo(anyLong(), anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/todo/posts/{id}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("todo-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),

                        pathParameters(
                                parameterWithName("id").description("todo 식별자")
                        )));

    }

    @Test
    @WithAuthUser
    void delete_todos() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        TodoIdsDto todoIdsDto = new TodoIdsDto(ids);

        String content = gson.toJson(todoIdsDto);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doNothing()
                .when(todoService).deleteTodos(anyList(), anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/todo/posts")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh","Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("todos-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestFields(
                                List.of(
                                        fieldWithPath("ids").type(JsonFieldType.ARRAY).description("삭제할 todo들의 식별자 리스트")
                                )
                        )));

    }



}
