package dev.orne.i18n.spring;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.support.RequestHandledEvent;

import dev.orne.i18n.I18nResources;
import dev.orne.i18n.context.I18nContext;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.i18n.context.I18nContextProviderStrategy;

/**
 * Unit tests for {@code I18nSpringWebContextClearer}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see I18nSpringWebContextClearer
 */
@Tag("ut")
class I18nSpringWebContextClearerTest {

    private static I18nContextProviderStrategy preTestsStrategy;

    private @Mock I18nContextProviderStrategy mockStrategy;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;
    private @Mock RequestHandledEvent event;
    protected AutoCloseable mocks;

    @BeforeAll
    static void saveDefaultStrategy() {
        preTestsStrategy = I18nContextProviderStrategy.getInstance();
    }

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    @AfterEach
    void restoreDefaultStrategy() {
        I18nContextProviderStrategy.setInstance(preTestsStrategy);
    }

    /**
     * Test {@link I18nSpringWebContextClearer#onApplicationEvent(RequestHandledEvent)}.
     */
    @Test
    void testClearContext() {
        I18nContextProviderStrategy.setInstance(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        final I18nSpringWebContextClearer listener = new I18nSpringWebContextClearer();
        listener.onApplicationEvent(event);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).clearContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }
}
