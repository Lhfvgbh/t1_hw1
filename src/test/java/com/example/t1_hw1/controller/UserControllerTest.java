package com.example.t1_hw1.controller;


import com.example.t1_hw1.aspect.TimeAspect;
import com.example.t1_hw1.aspect.TimeAsyncAspect;
import com.example.t1_hw1.domain.User;
import com.example.t1_hw1.model.request.UpsertUserRequest;
import com.example.t1_hw1.model.response.UserResponse;
import com.example.t1_hw1.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Тесты для {@link UserController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private TimeAspect timeAspect;

    @MockBean
    private TimeAsyncAspect timeAsyncAspect;

    @Before
    public void init() {
        AspectJProxyFactory factory = new AspectJProxyFactory(userService);
        factory.addAspect(timeAspect);
        factory.addAspect(timeAsyncAspect);
    }

    @Test
    public void should_success_getAllUsers() throws Exception {
        // given
        when(timeAspect.syncTimeStat(any())).thenReturn(
                List.of(new User().setId(1).setLogin("user1"),
                        new User().setId(2).setLogin("user2")));

        // when
        var actualResult = mockMvc.perform(get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        var resultJson = actualResult.andReturn().getResponse().getContentAsString();
        var foundUsers = new ObjectMapper().readValue(resultJson, new TypeReference<List<UserResponse>>() {});
        assertThat(foundUsers.size(), equalTo(2));

        // and
        verify(timeAspect, times(1)).syncTimeStat(any());
    }

    @Test
    public void should_success_getUserById() throws Exception {
        // given
        when(timeAspect.syncTimeStat(any())).thenReturn(new UserResponse().setId(1).setLogin("user1"));

        // when
        var actualResult = mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        var resultJson = actualResult.andReturn().getResponse().getContentAsString();
        var foundUser = new ObjectMapper().readValue(resultJson, UserResponse.class);
        assertThat(foundUser.getId(), equalTo(1));
        assertThat(foundUser.getLogin(), equalTo("user1"));

        // and
        verify(timeAspect, times(1)).syncTimeStat(any());
    }

    @Test
    public void should_success_createUser() throws Exception {
        // given
        var user = new UpsertUserRequest().setId(1).setLogin("user1").setFirstName("abc").setLastName("cde").setAge(23);
        var requestJson = new ObjectMapper().writer().writeValueAsString(user);

        when(timeAsyncAspect.asyncTimeStat(any())).thenReturn(new User().setId(1).setLogin("user1").setFirstName("abc").setLastName("cde").setAge(23));

        // when
        mockMvc.perform(post("/api/v1/users")
                .contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        verify(timeAsyncAspect, times(1)).asyncTimeStat(any());
    }

    @Test
    public void should_success_updateUser() throws Exception {
        // given
        var user = new User().setId(2).setLogin("user2").setFirstName("abc").setLastName("cde").setAge(23);
        var requestJson = new ObjectMapper().writer().writeValueAsString(user);

        when(timeAsyncAspect.asyncTimeStat(any())).thenReturn(user);

        // when
        mockMvc.perform(patch("/api/v1/users/2")
                .contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        verify(timeAsyncAspect, times(1)).asyncTimeStat(any());
    }
}
