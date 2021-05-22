package dev.orne.i18n;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 Orne Developments
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code I18nStringEditor}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nStringEditor
 */
@Tag("ut")
class I18nStringEditorTest {

    private static final int RND_STR_LENGTH = 20;

    private @Mock I18nString mockI18nString;
    protected AutoCloseable mocks;

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    /**
     * Test {@link I18nStringEditor#setAsText(String)}.
     */
    @Test
    void testSetAsText() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringEditor editor = new I18nStringEditor();
        editor.setAsText(text);
        final Object value = editor.getValue();
        assertTrue(value instanceof I18nFixedString);
        final I18nFixedString result = (I18nFixedString) value;
        assertEquals(text, result.get());
    }

    /**
     * Test {@link I18nStringEditor#setAsText(String)}.
     */
    @Test
    void testSetAsText_Null() {
        final I18nStringEditor editor = new I18nStringEditor();
        editor.setAsText(null);
        final Object value = editor.getValue();
        assertNull(value);
    }

    /**
     * Test {@link I18nStringEditor#getAsText()}.
     */
    @Test
    void testGetAsText() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(mockI18nString).get();
        final I18nStringEditor editor = new I18nStringEditor();
        editor.setValue(mockI18nString);
        final String result = editor.getAsText();
        assertEquals(text, result);
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nStringEditor#getAsText()}.
     */
    @Test
    void testGetAsText_Null() {
        final I18nStringEditor editor = new I18nStringEditor();
        editor.setValue(null);
        final String result = editor.getAsText();
        assertNull(result);
    }
}
