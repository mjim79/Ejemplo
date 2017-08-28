package com.mjim79.bartender.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

public class BarTenderExceptionTest {

    @Test
    public void shouldCreateABarTenderExceptionWithoutArments() {

        final BarTenderException result = new BarTenderException();

        assertThat(result instanceof BarTenderException, is(true));

    }

    @Test
    public void shouldCreateABarTenderExceptionWithCause() {

        final RuntimeException e = mock(RuntimeException.class);

        final BarTenderException result = new BarTenderException(e);

        assertThat(result.getCause(), is(e));

    }

    @Test
    public void shouldCreateABarTenderExceptionWithMessage() {

        final BarTenderException result = new BarTenderException("message");

        assertThat(result.getMessage(), is("message"));

    }

    @Test
    public void shouldCreateABarTenderExceptionWithCauseAndMessage() {

        final RuntimeException e = mock(RuntimeException.class);

        final BarTenderException result = new BarTenderException("message", e);

        assertThat(result.getMessage(), is("message"));
        assertThat(result.getCause(), is(e));

    }

}
