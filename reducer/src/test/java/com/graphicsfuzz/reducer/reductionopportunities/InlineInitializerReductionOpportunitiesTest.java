/*
 * Copyright 2018 The GraphicsFuzz Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graphicsfuzz.reducer.reductionopportunities;

import static org.junit.Assert.assertEquals;

import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import com.graphicsfuzz.common.util.ParseHelper;
import com.graphicsfuzz.common.util.RandomWrapper;
import java.util.List;
import org.junit.Test;

public class InlineInitializerReductionOpportunitiesTest {

  @Test
  public void testGlobalScope() throws Exception {
    final String program = "const int x = 2; void main() { int y = x + 3; }";
    final String expected = "const int x = 2; void main() { int y = (2) + 3; }";
    final TranslationUnit tu = ParseHelper.parse(program, false);
    final List<InlineInitializerReductionOpportunity> ops =
          InlineInitializerReductionOpportunities.findOpportunities(MakeShaderJobFromFragmentShader.make(tu), new ReductionOpportunityContext(true,
          ShadingLanguageVersion.ESSL_100, new RandomWrapper(0), null));
    assertEquals(1, ops.size());
    ops.get(0).applyReduction();
    assertEquals(PrettyPrinterVisitor.prettyPrintAsString(ParseHelper.parse(expected, false)),
          PrettyPrinterVisitor.prettyPrintAsString(tu));


  }
}