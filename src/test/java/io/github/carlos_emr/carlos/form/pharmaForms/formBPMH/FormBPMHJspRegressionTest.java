/**
 * Copyright (c) 2026 CARLOS EMR Contributors. All Rights Reserved.
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * CARLOS EMR Project
 * https://github.com/carlos-emr/carlos
 */
package io.github.carlos_emr.carlos.form.pharmaForms.formBPMH;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression coverage for issue #2636: formBPMH.jsp's "save" submit button
 * opened its {@code value} attribute with a Unicode left double quotation
 * mark ({@code “}) instead of an ASCII double quote. The mismatched
 * quote characters left the attribute unterminated, corrupting the rendered
 * markup for every control after the save button.
 *
 * @since 2026-07-20
 */
@DisplayName("formBPMH.jsp save button quoting")
@Tag("unit")
@Tag("form")
class FormBPMHJspRegressionTest {
    private static final String BASEDIR_PROPERTY = "basedir";
    private static final Path JSP_PATH = resolveProjectPath(
            Path.of("src/main/webapp/WEB-INF/jsp/form/pharmaForms/formBPMH.jsp"));

    @Test
    @DisplayName("should close the save button's value attribute with an ASCII double quote")
    void shouldUseAsciiQuotes_forSaveButtonValueAttribute() throws Exception {
        String jsp = Files.readString(JSP_PATH);

        assertThat(jsp).contains(
                "<input type=\"submit\" name=\"submit\" value=\"<fmt:message key=\"colcamex.formBPMH.save\"/>\" />");
    }

    @Test
    @DisplayName("should not reintroduce Unicode smart quote characters")
    void shouldNotContainUnicodeSmartQuotes_inSaveButtonMarkup() throws Exception {
        String jsp = Files.readString(JSP_PATH);

        assertThat(jsp).doesNotContain("“").doesNotContain("”");
    }

    private static Path resolveProjectPath(Path relativePath) {
        Path current = Path.of(System.getProperty(BASEDIR_PROPERTY, System.getProperty("user.dir")))
                .toAbsolutePath()
                .normalize();
        for (int checkedParents = 0; current != null && checkedParents < 6; checkedParents++) {
            Path candidate = current.resolve(relativePath).normalize();
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Unable to locate " + relativePath + " from "
                + System.getProperty(BASEDIR_PROPERTY, System.getProperty("user.dir")));
    }
}
