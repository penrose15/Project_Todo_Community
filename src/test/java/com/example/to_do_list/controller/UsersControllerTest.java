package com.example.to_do_list.controller;

import com.example.to_do_list.WithAuthUser;
import com.example.to_do_list.common.security.config.SecurityConfig;
import com.example.to_do_list.dto.user.UsersSaveDto;
import com.example.to_do_list.service.ChangePasswordService;
import com.example.to_do_list.service.UsersService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.time.LocalDate;
import java.util.List;

import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentRequest;
import static com.example.to_do_list.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UsersController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
public class UsersControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UsersService usersService;

        @MockBean
        private ChangePasswordService changePasswordService;

        @Autowired
        private Gson gson;
        @Test
        void joinUser() throws Exception {
                UsersSaveDto usersSaveDto = new UsersSaveDto("abc@gmail.com", "abcd1234!");


                doReturn("abc@gmail.com")
                        .when(usersService).createUsers(eq(usersSaveDto));

                String content = gson.toJson(usersSaveDto);

                ResultActions actions = mockMvc.perform(
                        post("/api/user/account")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

                actions
                        .andExpect(status().isCreated())
                        .andDo(document("users-signUp",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        List.of(
                                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비번")
                                        )
                                )
                                ));
        }

        @Test
        @WithAuthUser
        void joinTeam() throws Exception {
                Long teamId = 1L;

                doReturn(teamId)
                        .when(usersService).joinTeam(anyLong(), anyString());

                ResultActions actions = mockMvc.perform(
                        patch("/api/user/team/{teamId}", 1L)
                                .header("Authorization", "Bearer (accessToken)")
                                .header("Refresh","Bearer (refreshToken)")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

                actions
                        .andExpect(status().isOk())
                        .andDo(document("join-team",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken"),
                                        headerWithName("Refresh").description("refreshToken")),
                                pathParameters(
                                        parameterWithName("teamId").description("team 식별자")
                                )
                        ));


        }

        @Test
        @WithAuthUser
        void withdrawal_team() throws Exception {
                doNothing()
                        .when(usersService).resignTeam(anyString());

                ResultActions actions = mockMvc.perform(
                        patch("/api/user/withdrawal")
                                .header("Authorization", "Bearer (accessToken)")
                                .header("Refresh","Bearer (refreshToken)")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

                actions
                        .andExpect(status().isNoContent())
                        .andDo(document("withdrawal-team",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT AccessToken"),
                                        headerWithName("Refresh").description("JWT RefreshToken")
                                )
                                ));

        }
}
