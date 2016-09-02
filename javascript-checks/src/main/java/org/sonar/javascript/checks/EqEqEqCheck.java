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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.sonar.check.Rule;
import org.sonar.javascript.se.Constraint;
import org.sonar.javascript.se.ProgramState;
import org.sonar.javascript.se.SeCheck;
import org.sonar.javascript.se.Type;
import org.sonar.javascript.tree.symbols.Scope;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;

import static sun.tools.jconsole.Messages.MESSAGE;

@Rule(key = "EqEqEq")
public class EqEqEqCheck extends SeCheck {

  // For each "+" binary expression tree this map contains true if types of operands are string and non-string in all execution paths,
  // true if types are good in at least one execution path
  private Map<BinaryExpressionTree, Boolean> appropriateTypes = new HashMap<>();


  @Override
  public void beforeBlockElement(ProgramState currentState, Tree element) {
    if (element.is(Kind.EQUAL_TO, Kind.NOT_EQUAL_TO)) {
      BinaryExpressionTree equality = (BinaryExpressionTree) element;
      if (!isNullLiteral(equality.leftOperand()) && !isNullLiteral(equality.rightOperand())) {
        Constraint rightConstraint = currentState.getConstraint(currentState.peekStack(0));
        Constraint leftConstraint = currentState.getConstraint(currentState.peekStack(1));

        Type rightType = rightConstraint.type();
        Type leftType = leftConstraint.type();

        if (leftType != null && leftType == rightType) {
          return;
        }

        addIssue(equality.operator(), element.is(Kind.EQUAL_TO) ? "Replace \"==\" with \"===\"." : "Replace \"!=\" with \"!==\".");
      }
    }
  }

  @Override
  public void endOfExecution(Scope functionScope) {
    for (Entry<BinaryExpressionTree, Boolean> entry : appropriateTypes.entrySet()) {
      if (entry.getValue()) {
        BinaryExpressionTree tree = entry.getKey();
        addIssue(tree.operator(), MESSAGE)
          .secondary(tree.leftOperand())
          .secondary(tree.rightOperand());
      }
    }
  }



  private static boolean isNullLiteral(ExpressionTree expressionTree) {
    return expressionTree.is(Tree.Kind.NULL_LITERAL);
  }

}
