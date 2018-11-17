package com.app.weatherforecast

import com.app.weatherforecast.data.A
import com.app.weatherforecast.data.B
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.powermock.modules.junit4.PowerMockRunner

//@RunWith(PowerMockRunner::class)
class ClassATest {
    private lateinit var underTest :A
    private val mockB: B = mock<B> {
       on { printValue(anyString()) }.doReturn(125)
    }

    @Before
    fun setUp() {
//        whenever(mockB.printValue(Mockito.anyString())).thenReturn(125)
//        //whenever(mockB.printValue()).thenReturn(125)
//        //whenever(mockB.printValue(eq(""))).thenReturn(1000)
//        underTest = A(mockB)
    }

    @Test
    fun myawesomemethodTest() {

        //whenever(mockB.printValue(anyString())).thenReturn(125)
        underTest = A(mockB)
        underTest.myawesomemethod("testString")
    }
}