package com.example.to_do_list.controller;

import com.example.to_do_list.WithAuthUser;
import com.example.to_do_list.common.security.config.SecurityConfig;
import com.example.to_do_list.dto.category.CategoriesResponseDto;
import com.example.to_do_list.dto.category.CategorySaveDto;
import com.example.to_do_list.dto.category.CategoryUpdateDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.service.CategoryService;
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
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private UsersService usersService;
    @Autowired
    private Gson gson;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 7);

    @Test
    @WithAuthUser
    public void saveCategory() throws Exception {
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("category1")
                .explanation("explation1")
                .build();
        categorySaveDto.setUsersId(1L);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn("category1")
                .when(categoryService).save(any(CategorySaveDto.class));

        ResultActions actions = mockMvc.perform(
                post("/api/category/")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(categorySaveDto))
        );

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value("category1"))
                .andDo(document("create-category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        requestFields(
                                List.of(
                                        fieldWithPath("usersId").type(JsonFieldType.NUMBER).description("회원 식별자 아이디").ignored(),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                        fieldWithPath("explanation").type(JsonFieldType.STRING).description("카테고리 설명")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void updateCategory() throws Exception {
        CategoryUpdateDto categoryUpdateDto = CategoryUpdateDto.builder()
                .name("patch category1")
                .explanation("patch explanation1")
                .build();
        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn("patch category1")
                .when(categoryService).update(any(CategoryUpdateDto.class), anyLong(), anyLong());

        ResultActions actions = mockMvc.perform(
                patch("/api/category/{categoryId}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(categoryUpdateDto))
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("patch category1"))
                .andDo(document("update-category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                        fieldWithPath("explanation").type(JsonFieldType.STRING).description("카테고리 설명")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void addTodoListToCategory() throws Exception {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        TodoSaveDto todoSaveDto = TodoSaveDto.builder()
                .title("todo title")
                .content("todo content")
                .expose("PUBLIC")
                .priority(2)
                .endDate("2023-10-10")
                .build();


        doReturn("todo title")
                .when(categoryService).addTodoList(any(TodoSaveDto.class), anyLong(), anyString());

        ResultActions actions = mockMvc.perform(
                post("/api/category/{categoryId}/todo", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(todoSaveDto))
        );

        actions.andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data").value("todo title"))
                .andDo(document("add-todoList-to-Category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("Todo 내용"),
                                        fieldWithPath("expose").type(JsonFieldType.STRING).description("공개 여부"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("마감 날짜")
                                )
                        )
                        ));
    }

    @Test
    @WithAuthUser
    void showAllCategories() throws Exception {
        TodoResponsesDto todoResponsesDto1 = new TodoResponsesDto(1L, "title1", false);
        TodoResponsesDto todoResponsesDto2 = new TodoResponsesDto(2L, "title2", false);
        List<TodoResponsesDto> list1 = List.of(todoResponsesDto1, todoResponsesDto2);

        CategoriesResponseDto categoriesResponseDto1 = CategoriesResponseDto.builder()
                .name("category name1")
                .todoResponsesDtos(list1)
                .build();

        TodoResponsesDto todoResponsesDto3 = new TodoResponsesDto(3L, "title3", false);
        TodoResponsesDto todoResponsesDto4 = new TodoResponsesDto(4L, "title4", false);
        List<TodoResponsesDto> list2 = List.of(todoResponsesDto3, todoResponsesDto4);

        CategoriesResponseDto categoriesResponseDto2 = CategoriesResponseDto.builder()
                .name("category name2")
                .todoResponsesDtos(list2)
                .build();

        List<CategoriesResponseDto> responseDtos = List.of(categoriesResponseDto1, categoriesResponseDto2);

        doReturn(1L)
                .when(usersService).findByEmail(anyString());
        doReturn(responseDtos)
                .when(categoryService).showAllCategories(anyLong());

        ResultActions actions = mockMvc.perform(
                get("/api/category/categories")
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("show-all-categories",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("category-list"),
                                        fieldWithPath("data.[]name").type(JsonFieldType.STRING).description("category 이름"),
                                        fieldWithPath("data.[]todoResponsesDtos").type(JsonFieldType.ARRAY).description("카테고리에 해당되는 Todo들"),
                                        fieldWithPath("data.[]todoResponsesDtos.[]id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                        fieldWithPath("data.[]todoResponsesDtos.[]title").type(JsonFieldType.STRING).description("todo 제목"),
                                        fieldWithPath("data.[]todoResponsesDtos.[]status").type(JsonFieldType.BOOLEAN).description("todo 완료 여부")
                                )
                        )));

    }

    @Test
    @WithAuthUser
    void deleteCategory() throws Exception {
        doNothing()
                .when(categoryService).deleteCategory(anyLong(), anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/category/{categoryId}", 1L)
                        .header("Authorization", "Bearer (accessToken)")
                        .header("Refresh", "Bearer (refreshToken)")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")),
                        pathParameters(
                                parameterWithName("categoryId").description("category 식별자")
                        )));
    }


}
