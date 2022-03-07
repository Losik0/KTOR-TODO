package com.example

import io.ktor.http.*
import io.ktor.application.*
import kotlin.test.*
import io.ktor.server.testing.*

class TodoRouteTests {

    @Test
    fun getAllTodo() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/todo").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun getTodo() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/todo/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("""{"id":1,"contents":"first contents","is_finished":false}""", response.content)
            }
        }
    }

    @Test
    fun postTodo(){
        withTestApplication(Application::module) {
            with(handleRequest(HttpMethod.Post, "/todo"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("contents" to "test-contents").formUrlEncode())
            })
            {
                //assertEquals(HttpStatusCode.Created, response.status())
            }
        }
    }

    @Test
    fun deleteTodo() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Delete, "/todo/1").apply {
                assertEquals(HttpStatusCode.Accepted, response.status())
                assertEquals("Todo Deleted", response.content)
            }
        }
    }

    @Test
    fun completeTodo(){
        withTestApplication(Application::module) {
            with(handleRequest(HttpMethod.Put, "/completed/3"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            })
            {
                assertEquals(HttpStatusCode.Accepted, response.status())
                assertEquals("Todo finished", response.content)
            }
        }
    }

    @Test
    fun getCompletedTodo(){
        withTestApplication(Application::module) {
            with(handleRequest(HttpMethod.Get, "/completed"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            })
            {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("""[{"id":2,"contents":"eat buffalo wings","is_finished":true}]""", response.content)
            }
        }
    }

    @Test
    fun getUnCompletedTodo(){
        withTestApplication(Application::module) {
            with(handleRequest(HttpMethod.Get, "/active"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            })
            {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun deleteCompletedTodo(){
        withTestApplication(Application::module) {
            with(handleRequest(HttpMethod.Delete, "/completed"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            })
            {
                assertEquals(HttpStatusCode.Accepted, response.status())
                assertEquals("Completed Todo Deleted", response.content)
            }
        }
    }

}