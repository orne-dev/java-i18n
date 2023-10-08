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

import java.time.Duration;
import java.util.HashSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.params.GenerationParameters;

/**
 * Unit tests for {@code I18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see I18nString
 */
@Tag("ut")
class I18nStringTest {

    /**
     * Test {@link I18nString#fixed(String)}.
     */
    @Test
    void testFixed() {
        assertNull(I18nString.fixed((String) null));
        final String text = Generators.randomValue(String.class);
        final I18nFixedString result = I18nString.fixed(text);
        assertNotNull(result);
        assertSame(I18nFixedString.from(text), result);
    }

    /**
     * Test {@link I18nString#mapped(String)}.
     */
    @Test
    void testMapped() {
        assertThrows(NullPointerException.class, () -> {
            I18nString.mapped((String) null);
        });
        final String text = Generators.randomValue(String.class);
        final I18nStringMap result = I18nString.mapped(text);
        assertEquals(new I18nStringMap(text), result);
    }

    /**
     * Test {@link I18nString#fromResources(String)}.
     */
    @Test
    void testForDefault() {
        assertThrows(NullPointerException.class, () -> {
            I18nString.fromResources(null);
        });
        final String text = Generators.randomValue(String.class);
        final I18nResourcesString.Builder result = I18nString.fromResources(text);
        assertEquals(I18nResourcesString.forDefault(text), result);
    }

    /**
     * Test for {@link I18nStringGenerator#defaultValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDefaultGeneration()
    throws Throwable {
        final I18nString result = Generators.defaultValue(I18nString.class);
        assertEquals(Generators.defaultValue(I18nFixedString.class), result);
    }

    /**
     * Test for {@link I18nStringGenerator#randomValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testRandomGeneration()
    throws Throwable {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            final HashSet<String> texts = new HashSet<>();
            boolean fixed = false;
            boolean mapped = false;
            boolean resources = false;
            while (texts.size() < 100 ||
                    !fixed ||
                    !mapped ||
                    !resources) {
                final I18nString result = Generators.randomValue(I18nString.class);
                assertNotNull(result);
                final String text = result.get();
                assertNotNull(text);
                texts.add(text);
                if (result instanceof I18nFixedString) {
                    fixed = true;
                } else if (result instanceof I18nStringMap) {
                    mapped = true;
                } else if (result instanceof I18nResourcesString) {
                    resources = true;
                }
            }
        });
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            final HashSet<String> texts = new HashSet<>();
            boolean fixed = false;
            boolean mapped = false;
            boolean resources = false;
            while (texts.size() < 100 ||
                    !fixed ||
                    !mapped ||
                    !resources) {
                final I18nString result = Generators.randomValue(
                        I18nString.class,
                        GenerationParameters.forSizes().withMinSize(5).withMaxSize(10));
                assertNotNull(result);
                final String text = result.get();
                assertNotNull(text);
                assertTrue(text.length() >= 5);
                assertTrue(text.length() <= 10);
                texts.add(text);
                if (result instanceof I18nFixedString) {
                    fixed = true;
                } else if (result instanceof I18nStringMap) {
                    mapped = true;
                } else if (result instanceof I18nResourcesString) {
                    resources = true;
                }
            }
        });
    }
}
