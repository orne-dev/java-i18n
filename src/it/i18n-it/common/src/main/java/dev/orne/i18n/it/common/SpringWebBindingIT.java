package dev.orne.i18n.it.common;

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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.context.I18nContextProviderStrategy;

/**
 * Spring Web binding test for {@code I18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nString
 */
@Tag("spring")
public class SpringWebBindingIT {

    private static final int RND_STR_LENGTH = 20;
    private static final String XX_LANG = "xx";
    private static final String YY_LANG = "yy";

    @AfterEach
    void resetI18N() {
        I18nContextProviderStrategy.setInstance(null);
    }

    @Test
    void testNullBinding() {
        final I18nStringContainer container = new I18nStringContainer();
        final Map<String, Object> values = new HashMap<>();
        values.put(I18nStringContainer.BEAN_PROPERTY, null);
        final MutablePropertyValues pvs = new MutablePropertyValues(values);
        final DataBinder binder = new DataBinder(container);
        binder.bind(pvs);
        assertNull(container.getBean());
    }

    @Test
    void testSimpleBinding() {
        final I18nStringContainer container = new I18nStringContainer();
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, Object> values = new HashMap<>();
        values.put(I18nStringContainer.BEAN_PROPERTY, text);
        final MutablePropertyValues pvs = new MutablePropertyValues(values);
        final DataBinder binder = new DataBinder(container);
        binder.bind(pvs);
        assertNotNull(container.getBean());
        assertTrue(container.getBean() instanceof I18nFixedString);
        assertEquals(text, container.getBean().get());
    }

    @Test
    void testNullMapBinding() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final Map<String, Object> values = new HashMap<>();
        values.put(I18nStringContainer.BEAN_PROPERTY, null);
        final MutablePropertyValues pvs = new MutablePropertyValues(values);
        final DataBinder binder = new DataBinder(container);
        binder.bind(pvs);
        assertNull(container.getBean());
    }

    @Test
    void testSimpleMapBinding() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, Object> values = new HashMap<>();
        values.put(I18nStringContainer.BEAN_PROPERTY, text);
        final MutablePropertyValues pvs = new MutablePropertyValues(values);
        final DataBinder binder = new DataBinder(container);
        binder.bind(pvs);
        assertNotNull(container.getBean());
        assertTrue(container.getBean() instanceof I18nStringMap);
        final I18nStringMap result = (I18nStringMap) container.getBean();
        assertEquals(text, result.getDefaultText());
        assertTrue(result.getI18n().isEmpty());
    }

    @Test
    void testComplexBinding() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, Object> values = new HashMap<>();
        values.put(I18nStringContainer.BEAN_PROPERTY + ".defaultText", text);
        values.put(I18nStringContainer.BEAN_PROPERTY + ".i18n[" + XX_LANG + "]", xxText);
        values.put(I18nStringContainer.BEAN_PROPERTY + ".i18n[" + YY_LANG + "]", yyText);
        final MutablePropertyValues pvs = new MutablePropertyValues(values);
        final DataBinder binder = new DataBinder(container);
        binder.bind(pvs);
        assertNotNull(container.getBean());
        assertTrue(container.getBean() instanceof I18nStringMap);
        final I18nStringMap result = (I18nStringMap) container.getBean();
        assertEquals(text, result.getDefaultText());
        assertEquals(xxText, result.get(XX_LANG));
        assertEquals(yyText, result.get(YY_LANG));
    }
}
