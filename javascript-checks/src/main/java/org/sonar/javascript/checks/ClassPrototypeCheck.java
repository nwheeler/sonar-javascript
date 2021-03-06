/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.javascript.api.symbols.Type;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.DotMemberExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.IdentifierTree;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "S3525")
public class ClassPrototypeCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Declare a \"%s\" class and move this declaration of \"%s\" into it.";

  @Override
  public void visitAssignmentExpression(AssignmentExpressionTree tree) {
    if (tree.variable().is(Kind.DOT_MEMBER_EXPRESSION) && tree.expression().types().containsOnlyAndUnique(Type.Kind.FUNCTION)) {
      DotMemberExpressionTree lhs = (DotMemberExpressionTree) tree.variable();

      if (lhs.object().is(Kind.DOT_MEMBER_EXPRESSION)) {
        DotMemberExpressionTree prototype = (DotMemberExpressionTree) lhs.object();

        if ("prototype".equals(prototype.property().name()) && prototype.object().is(Kind.IDENTIFIER_REFERENCE) && prototype.object().types().contains(Type.Kind.FUNCTION)) {
          addIssue(lhs, String.format(MESSAGE, ((IdentifierTree) prototype.object()).name(), lhs.property().name()));
        }
      }
    }
    super.visitAssignmentExpression(tree);
  }
}
