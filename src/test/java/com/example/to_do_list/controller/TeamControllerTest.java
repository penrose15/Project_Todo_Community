package com.example.to_do_list.controller;

import com.example.to_do_list.WithAuthUser;
import com.example.to_do_list.common.security.config.SecurityConfig;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.dto.team.*;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import com.example.to_do_list.dto.user.UsersMandateDto;
import com.example.to_do_list.service.TeamService;
import com.example.to_do_list.service.TodoService;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.xml.transform.Result;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentRequest;
import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TeamController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
public class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamService teamService;
    @MockBean
    private UsersService usersService;
    @Autowired
    private Gson gson;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 1);

    @Test
    @WithAuthUser
    public void postTest() throws Exception {
        TeamSaveDto teamSaveDto = TeamSaveDto.builder()
                .title("title")
                .explanation("투두리스트와 달성도를 다른 사람들과 공유하면서 동기부여를 일으키세요")
                .limit(6)
                .criteria(0)
                .build();
        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(1L)
                .when(teamService).save(any(TeamSaveDto.class), anyLong());

        ResultActions actions = mockMvc.perform(
                post("/api/team")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(teamSaveDto))
        );

        actions.andExpect(status().isCreated())
                .andDo(document("create-team",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("explanation").type(JsonFieldType.STRING).description("팀 설명 및 규칙"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("팀 인원 제한"),
                                        fieldWithPath("criteria").type(JsonFieldType.NUMBER).description("강퇴 기준")
                                )
                        )));
    }

    @Test
    @WithAuthUser
    void updateTeam() throws Exception {
        TeamUpdateDto updateDto = TeamUpdateDto.builder()
                .title("patch title")
                .explanation("수정된 팀 설명글")
                .limit(5)
                .criteria(2)
                .build();
        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(1L)
                .when(teamService).update(any(TeamUpdateDto.class), anyLong(), anyLong());

        ResultActions actions = mockMvc.perform(
                patch("/api/team/{id}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateDto)));
        actions.andExpect(status().isOk())
                .andDo(document("update-team",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("id").description("team 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("explanation").type(JsonFieldType.STRING).description("수정된 팀 설명글"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("팀 인원 제한"),
                                        fieldWithPath("criteria").type(JsonFieldType.NUMBER).description("강퇴 기준")
                                )
                        )));
    }

    @Test
    @WithAuthUser
    void showTeamDetail() throws Exception {
        TeamResponseDto responseDto = TeamResponseDto.builder()
                .teamId(1L)
                .title("팀 이름")
                .explanation("팀 설명")
                .limit(5)
                .criteria(5)
                .build();

        doReturn(responseDto)
                .when(teamService).showTeamDetails(anyLong());

        ResultActions actions = mockMvc.perform(
                get("/api/team/{id}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("show team detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("id").description("team 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("team 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("team 이름"),
                                        fieldWithPath("explanation").type(JsonFieldType.STRING).description("team 설명"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("team 인원 제한"),
                                        fieldWithPath("criteria").type(JsonFieldType.NUMBER).description("강퇴 기준")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void showTeamList() throws Exception {

        TeamResponsesDto teamResponsesDto1 = new TeamResponsesDto(1L, "team name", "team explanation", 5);
        TeamResponsesDto teamResponsesDto2 = new TeamResponsesDto(2L, "team name1", "team explanation2", 3);
        List<TeamResponsesDto> teamResponsesDtos = new ArrayList<>();
        teamResponsesDtos.add(teamResponsesDto1);
        teamResponsesDtos.add(teamResponsesDto2);

        PageRequest pageRequest = PageRequest.of(1, 10, Sort.Direction.DESC,"teamId");
        Page<TeamResponsesDto> slice = new PageImpl<>(teamResponsesDtos,pageRequest, teamResponsesDtos.size());
        doReturn(slice)
                .when(teamService).teamList(1, 10);
        ResultActions actions = mockMvc.perform(
                get("/api/team/list")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .param("page","1")
                        .param("size","10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(teamResponsesDtos))
        );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageInfo.page").value(2))
                .andExpect(jsonPath("$.pageInfo.size").value(10))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(12))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(2))
                .andDo(document("show team List",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("team-list"),
                                        fieldWithPath("data.[]teamId").type(JsonFieldType.NUMBER).description("team 식별자"),
                                        fieldWithPath("data.[]title").type(JsonFieldType.STRING).description("team 이름"),
                                        fieldWithPath("data.[]explanation").type(JsonFieldType.STRING).description("team 설명"),
                                        fieldWithPath("data.[]limits").type(JsonFieldType.NUMBER).description("team 인원 제한"),
                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("page"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("size"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("totalPages")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void showUsersTodoList() throws Exception {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        TodoTitleResponsesDto todoTitleResponsesDto = new TodoTitleResponsesDto(1L, "title1", true);
        TodoTitleResponsesDto todoTitleResponsesDto1 = new TodoTitleResponsesDto(2L, "title2", false);
        TodoTitleResponsesDto todoTitleResponsesDto2 = new TodoTitleResponsesDto(3L, "title3", true);
        TodoTitleResponsesDto todoTitleResponsesDto3 = new TodoTitleResponsesDto(4L, "title4", false);
        List<TodoTitleResponsesDto> todoTitleResponsesDtos = new ArrayList<>();
        List<TodoTitleResponsesDto> todoTitleResponsesDtos1 = new ArrayList<>();
        todoTitleResponsesDtos.add(todoTitleResponsesDto);
        todoTitleResponsesDtos.add(todoTitleResponsesDto1);
        todoTitleResponsesDtos1.add(todoTitleResponsesDto2);
        todoTitleResponsesDtos1.add(todoTitleResponsesDto3);
        UsersTodoDto usersTodoDto = UsersTodoDto.builder()
                .todoList(todoTitleResponsesDtos)
                .usersId(1L)
                .username("Justin.Oh")
                .build();
        UsersTodoDto usersTodoDto1 = UsersTodoDto.builder()
                .todoList(todoTitleResponsesDtos1)
                .usersId(2L)
                .username("JeongSu.Park")
                .build();
        List<UsersTodoDto> todoDtos = new ArrayList<>();
        todoDtos.add(usersTodoDto);
        todoDtos.add(usersTodoDto1);
        TeamDetailResponseDto teamDetailResponseDto = TeamDetailResponseDto.builder()
                .usersTodoDtos(todoDtos)
                .teamId(1L)
                .title("team name")
                .explanation("team explanation")
                .build();

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(teamDetailResponseDto)
                .when(teamService).showUsersTodoList(anyLong(), eq(LOCAL_DATE));

        ResultActions actions = mockMvc.perform(
                get("/api/team/todoList/{id}/{date}", 1L, "2023-02-01")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(jsonPath("$.data.teamId").value(teamDetailResponseDto.getTeamId()))
                .andExpect(jsonPath("$.data.title").value(teamDetailResponseDto.getTitle()))
                .andExpect(jsonPath("$.data.explanation").value(teamDetailResponseDto.getExplanation()))
                .andExpect(jsonPath("$.data.usersTodoDtos").isArray())
                .andDo(document("showUsersTodoList",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("id").description("team 식별자"),
                                parameterWithName("date").description("날짜")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data.teamId").type(JsonFieldType.NUMBER).description("team 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("data.explanation").type(JsonFieldType.STRING).description("팀 설명"),
                                        fieldWithPath("data.usersTodoDtos").type(JsonFieldType.ARRAY).description("팀원들 투두리스트"),
                                        fieldWithPath("data.usersTodoDtos.[]usersId").type(JsonFieldType.NUMBER).description("팀원 식별자"),
                                        fieldWithPath("data.usersTodoDtos.[]username").type(JsonFieldType.STRING).description("팀원 이름"),
                                        fieldWithPath("data.usersTodoDtos.[]todoList").type(JsonFieldType.ARRAY).description("팀원의 투두리스트"),
                                        fieldWithPath("data.usersTodoDtos.[]todoList.[]id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("data.usersTodoDtos.[]todoList.[]title").type(JsonFieldType.STRING).description("todo 제목"),
                                        fieldWithPath("data.usersTodoDtos.[]todoList.[]status").type(JsonFieldType.BOOLEAN).description("Todo 실행/미실행")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void mandateHost() throws Exception {
        UsersMandateDto mandateDto = new UsersMandateDto(1L);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(1L)
                .when(teamService).mandateHost(anyLong(), anyLong(), anyLong());

        ResultActions actions = mockMvc.perform(
                patch("/api/team/host")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .param("teamId", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mandateDto))
        );

        actions.andExpect(status().isOk())
                .andDo(document("mandate team host",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestParameters(
                                parameterWithName("teamId").description("team식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("usersId").type(JsonFieldType.NUMBER).description("방장 권한 위임할 대상 식별자")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("방장 권한 위임할 대상 식별자")
                                )
                        )
                        ));
    }



}
